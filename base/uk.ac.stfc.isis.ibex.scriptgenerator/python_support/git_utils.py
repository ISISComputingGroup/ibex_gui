import os
import pathlib
from urllib.parse import urlparse
from git import Repo, Git
from git.exc import NoSuchPathError, GitCommandError

from git.exc import InvalidGitRepositoryError

DEFAULT_REPO_PATH = os.path.join(pathlib.Path(__file__).parent.absolute(), "ScriptDefinitions")
REMOTE_URL = "https://github.com/ISISComputingGroup/ScriptDefinitions.git"
OLD_REPOSITORY = "https://github.com/ISISComputingGroup/ScriptGeneratorConfigs.git"

# Repository bundle is placed in python support folder on static script gen build
DEFAULT_BUNDLE_PATH = os.path.join(pathlib.Path(__file__).parent.absolute(), "ScriptDefinitions_repo.bundle")


class DefinitionsRepository:
    """
    Contains methods to initialise and maintain the script definitions repository
    """

    def __init__(self, directory_path: str = DEFAULT_REPO_PATH, remote_url: str = REMOTE_URL, bundle_path: str = DEFAULT_BUNDLE_PATH):
        self.path = directory_path
        self.remote_url = remote_url
        self.bundle_path = bundle_path
        self.git = Git()

        self.errors = []

    def _repo_already_exists(self) -> bool:
        """
        Checks if there is a repository initialised at the supplied path with the correct url
        """
        definitions_repo = None
        try:
            definitions_repo = Repo(self.path)
            origin_url = definitions_repo.remotes["origin"].url
        except (NoSuchPathError, InvalidGitRepositoryError):
            pass

        if definitions_repo is not None and urlparse(origin_url) == urlparse(self.remote_url):
            repo_exists = True
        elif definitions_repo is not None and urlparse(origin_url) == urlparse(OLD_REPOSITORY):
            # Repo containing script definitions was renamed, so accept URLs from old repo and change later
            repo_exists = True
        else:
            repo_exists = False

        return repo_exists

    def initialise_and_pull(self):
        """
        Attempts to clone the repo if it doesn't already exist, then pull from origin
        """

        if not self._repo_already_exists():
            self._attempt_repo_init()

        try:
            repo = Repo(self.path)
        except (NoSuchPathError, GitCommandError, InvalidGitRepositoryError) as err:
            self._append_error("Could not pull from origin, error was {}".format(err))
        else:
            if urlparse(repo.remotes["origin"].url) == urlparse(OLD_REPOSITORY):
                self._change_origin_url(repo)

            self._pull_from_origin(repo)

    def _pull_from_origin(self, repo: Repo):
        """
        If the supplied path is a valid script defintions repository, attempt to pull from origin

        Parameters:
            repo: git repo object representing the script definitions repository
        """
        origin = repo.remotes["origin"]

        try:
            origin.pull()
        except (GitCommandError, InvalidGitRepositoryError):
            self._append_error("Local repo contains unpushed changes, cannot pull from remote")

            if repo.is_dirty():
                # Run git merge --abort to undo changes
                repo.git.merge(abort=True)

    def clone_repo_from_bundle(self):
        """
        Unbundles the repository supplied with the release, sets remote URL to upstream

        Returns:
            script_definitions_repo: The newly cloned repository
        """
        try:
            self.git.clone(self.bundle_path, self.path)
        except GitCommandError as err:
            self._append_error("Error cloning repository from bundle: {}".format(err))

        try:
            script_definitions_repo = Repo(self.path)
            script_definitions_repo.delete_remote("origin")
            script_definitions_repo.create_remote("origin", self.remote_url)
        except (NoSuchPathError, GitCommandError, InvalidGitRepositoryError) as err:
            self._append_error("Cloning new repository failed with error: {}".format(err))
        else:
            self._change_origin_url(script_definitions_repo)

    def _change_origin_url(self, repo: Repo):
        """
        Changes the origin URL of the supplied repo
        """
        try:
            repo.delete_remote("origin")
            repo.create_remote("origin", self.remote_url)
        except (GitCommandError, InvalidGitRepositoryError) as err:
            self._append_error("Could not change remote origin of repo: {}".format(err))

    def _attempt_repo_init(self):
        """
        Attempt to clone new repository if the supplied path is not initialised to the script definitions URL

        Paramters:
            path: Location of directory to initialise

        Raises:
            OSError If the supplied directory is not empty
        """
        if not os.path.isdir(self.path):
            os.makedirs(self.path, exist_ok=True)

        if len(os.listdir(self.path)) > 0:
            self._append_error("Supplied directory {} not empty, cannot clone".format(self.path))
        else:
            try:
                self.git.clone(self.remote_url, self.path)
            except GitCommandError as err:
                self._append_error(str(err))
                self.clone_repo_from_bundle()

    def _append_error(self, error: str):
        """
        Adds the supplied error to the list of errors raised from the git repository
        """
        self.errors.append(error)
