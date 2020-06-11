import os
from git import Repo, Git
from git.exc import NoSuchPathError, GitCommandError

from git.exc import InvalidGitRepositoryError

DEFAULT_REPO_PATH = "C:\\ScriptDefinitions"
DEFAULT_BUNDLE_PATH = os.path.join(os.path.abspath(os.getcwd()), "ScriptDefinitions_repo.bundle")
REMOTE_URL = "https://github.com/ISISComputingGroup/ScriptDefinitions.git"


class DefinitionsRepository:
    """
    Contains methods to initialise and maintain the script definitions repository
    """

    def __init__(self, directory_path: str = DEFAULT_REPO_PATH, remote_url: str = REMOTE_URL, bundle_path: str = DEFAULT_BUNDLE_PATH):
        self.path = directory_path
        self.remote_url = remote_url
        self.bundle_path = bundle_path
        self.git = Git()

        #self.repo = None

        self.errors = []

    def _repo_already_exists(self) -> bool:
        """
        Checks if there is a repository initialised at the supplied path with the correct url
        """
        definitions_repo = None
        try:
            definitions_repo = Repo(self.path)
        except (NoSuchPathError, InvalidGitRepositoryError):
            pass

        print(definitions_repo)

        if definitions_repo is not None and definitions_repo.remotes['origin'].url == self.remote_url:
            repo_exists = True
        else:
            repo_exists = False
            self._append_error("Supplied path does not contain a ScriptDefinitions repository, cannot pull")

        return repo_exists

    def initialise_and_pull(self):
        """
        Attempts to clone the repo if it doesn't already exist, then pull from origin
        """

        if not self._repo_already_exists():
            self._attempt_repo_init()

        self.repo = Repo(self.path)

        self.pull_from_origin(Repo(self.path))

    def pull_from_origin(self, repo: Repo):
        """
        If the supplied path is a valid script defintions repository, attempt to pull from origin
        """
        if self._repo_already_exists():
            #repo = Repo(self.path)

            origin = repo.remotes['origin']

            try:
                origin.pull()
            except GitCommandError:
                self._append_error("Local repo contains unpushed changes, cannot pull from remote")

                # Run git merge --abort to undo changes
                repo.git.merge(abort=True)

    def clone_repo_from_bundle(self) -> Repo:
        """
        Unbundles the repository supplied with the release, sets remote URL to upstream

        Parameters:
            bundle_path: Location of the git bundle containing the script defs repo
            new_repo_path: Where to initialise the script definitions repository
            origin_url: The URL pointing to the upstream repository

        Returns:
            script_definitions_repo: The newly cloned repository
        """
        try:
            self.git.clone(self.bundle_path, self.path)
        except GitCommandError:
            self._append_error("Error cloning repository from bundle")

        script_definitions_repo = Repo(self.path)
        script_definitions_repo.delete_remote('origin')
        script_definitions_repo.create_remote('origin', self.remote_url)

        return script_definitions_repo

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
            self._append_error("Supplied directory {} not empty, cannot clone".format(path))
        else:
            try:
                self.git.clone(self.remote_url, self.path)
            except GitCommandError as err:
                self._append_error(err)
                self.clone_repo_from_bundle()

    def _append_error(self, error: str):
        """
        Adds the supplied error to the list of errors raised from the git repository
        """
        self.errors.append(error)
