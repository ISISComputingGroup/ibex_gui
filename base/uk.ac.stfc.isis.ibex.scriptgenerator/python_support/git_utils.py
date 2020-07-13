import os
import pathlib
from urllib.parse import urlparse
from typing import Optional

from git.exc import NoSuchPathError, GitCommandError, InvalidGitRepositoryError

SCRIPT_GEN_FOLDER = pathlib.Path(__file__).parent.absolute()

try:
    from git import Repo, Git, FetchInfo
except ImportError:
    # git not on system PATH, use portable git distribution
    GIT_EXECUTABLE_PATH = os.path.join(SCRIPT_GEN_FOLDER, "git", "cmd", "git.exe")
    os.environ["GIT_PYTHON_GIT_EXECUTABLE"] = GIT_EXECUTABLE_PATH
    from git import Repo, Git

DEFAULT_REPO_PATH = os.path.join(SCRIPT_GEN_FOLDER, "ScriptDefinitions")
REMOTE_URL = "https://github.com/ISISComputingGroup/ScriptDefinitions.git"
OLD_REPOSITORY = "https://github.com/ISISComputingGroup/ScriptGeneratorConfigs.git"

# Repository bundle is placed in python support folder on static script gen build
DEFAULT_BUNDLE_PATH = os.path.join(SCRIPT_GEN_FOLDER, "ScriptDefinitions_repo.bundle")


class DefinitionsRepository:
    """
    Contains methods to initialise and maintain the script definitions repository
    """

    def __init__(self, path: str = DEFAULT_REPO_PATH, remote_url: str = REMOTE_URL, bundle_path: str = DEFAULT_BUNDLE_PATH):
        self.path = path
        self.remote_url = remote_url
        self.bundle_path = bundle_path

        self.git = Git()
        self.errors = []

        self.branch = "master"

        self.is_dirty = False
        self.repo = self.initialise_repo()

        try:
            # Fetch and store information about state of origin
            self.fetch_info = self.fetch_from_origin()
            #self.repo.remotes['origin'].fetch()
        except GitCommandError:
            self._append_error("Remote URL could not be reached or is not a valid git repository")
            self.remote_available = False
        except ValueError as err:
            self._append_error(err)
            self.remote_available = False
        else:
            #self.fetch_info = self.fetch_from_origin()
            self.remote_available = True

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
        else:
            repo_exists = False

        return repo_exists

    def initialise_repo(self) -> Optional[Repo]:
        """
        Creates a git python Repo object representing the definitions repo. The repo is cloned if it does not exist
        """

        if not self._repo_already_exists():
            self._attempt_repo_init()

        repo = None

        try:
            repo = Repo(self.path)
        except (NoSuchPathError, GitCommandError, InvalidGitRepositoryError) as err:
            self._append_error("Error occured initialing repository: {}".format(err))

        return repo

    def reset_to_origin_master(self) -> None:
        """
        Performs a reset to the lastest origin/master
        """
        pass

    def updates_available(self) -> bool:
        """
        Returns True if the local repository has a different commit ID to origin
        """

        if self.remote_available:
            origin_commit_id = self.fetch_info.commit
            local_commit_id = self.repo.head.commit
            updates_available = local_commit_id != origin_commit_id
        else:
            updates_available = False

        return updates_available

        return True
        return local_commit_id != origin_commit_id

    def fetch_from_origin(self) -> FetchInfo:
        """
        Fetches latest changes from origin. Returns FetchInfo object for current branch

        Returns:
        fetch_info: FetchInfo object containing the state of the corresponding branch on origin

        Raises:
        ValueError if origin does not contain a branch with the same name as the current branch
        """
        origin = self.repo.remotes["origin"]

        all_origin_branches = origin.fetch()

        # Pick out the origin branch with the same name as the local branch
        fetch_info = [branch for branch in all_origin_branches if branch.name == "origin/{}".format(self.branch)]

        if len(fetch_info) == 1:
            branch_info = fetch_info[0]
        elif len(fetch_info) == 0:
            self._append_error("No branches were found in origin with name {branch}".format(branch=self.branch))
            branch_info = None
        elif len(fetch_info) > 1:
            self._append_error("Multiple branches found in origin with name {branch}".format(branch=self.branch))
            branch_info = None

        return branch_info

    def pull_from_origin(self):
        """
        If the supplied path is a valid script defintions repository, attempt to pull from origin

        Parameters:
            repo: git repo object representing the script definitions repository
        """
        origin = self.repo.remotes["origin"]

        try:
            origin.pull()
        # Capture this error and present it to the user in the dialogue box 
        except (GitCommandError, InvalidGitRepositoryError) as err:
            self._append_error("Local repo contains unpushed changes, cannot pull from remote")

            if self.repo.is_dirty():
                # Run git merge --abort to undo changes
                self.repo.git.merge(abort=True)
                self.is_dirty = True
            else:
                self.is_dirty = False

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
            self._change_origin_url()

    def _change_origin_url(self):
        """
        Changes the origin URL of the supplied repo
        """
        try:
            self.repo.delete_remote("origin")
            self.repo.create_remote("origin", self.remote_url)
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
