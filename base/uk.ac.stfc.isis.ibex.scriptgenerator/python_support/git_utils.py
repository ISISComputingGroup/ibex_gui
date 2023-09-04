import os
import pathlib
from urllib.parse import urlparse
from typing import Optional

SCRIPT_GEN_FOLDER = pathlib.Path(__file__).parent.absolute()

try:
    import git
except ImportError:
    # git not on system PATH, use portable git distribution
    GIT_EXECUTABLE_PATH = os.path.join(SCRIPT_GEN_FOLDER, "git", "cmd", "git.exe")
    os.environ["GIT_PYTHON_GIT_EXECUTABLE"] = GIT_EXECUTABLE_PATH

from git.exc import NoSuchPathError, GitCommandError, InvalidGitRepositoryError, GitError
from git import Repo, Git, FetchInfo

# This initialises the script_definitions folder next to the built executables
DEFAULT_REPO_PATH = SCRIPT_GEN_FOLDER.parent.parent.parent / 'script_definitions'
REMOTE_URL = "https://github.com/ISISComputingGroup/ScriptDefinitions.git"
OLD_REPOSITORY = "https://github.com/ISISComputingGroup/ScriptGeneratorConfigs.git"

# Repository bundle is placed in python support folder on static script gen build
DEFAULT_BUNDLE_PATH = os.path.join(SCRIPT_GEN_FOLDER, "ScriptDefinitions_repo.bundle")


class DefinitionsRepository:
    """
    Contains methods to initialise and maintain the script definitions repository
    """

    def __init__(self, path, remote_url: str = REMOTE_URL, bundle_path: str = DEFAULT_BUNDLE_PATH, branch=None):
        self.errors = []
        if path is not None and os.path.exists(path):
            self.path = path
        else:
            self.path = DEFAULT_REPO_PATH
            
        self.remote_url = remote_url
        self.bundle_path = bundle_path

        self.git = Git()

        self.repo = self.initialise_repo()

        self.branch = 'master'

        try:
            self.fetch_info = self.fetch_from_origin()
        except GitCommandError:
            self._append_error("Remote URL could not be reached or is not a valid git repository")
            self.fetch_info = None
        except ValueError as err:
            self._append_error(err)
            self.fetch_info = None

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
            self._append_error("Error occurred initialising repository: {}".format(err))

        return repo

    def is_dirty(self) -> bool:
        """
        Returns True if the git repository has uncommitted changes
        """
        return self.repo.is_dirty()

    def updates_available(self) -> bool:
        """
        Returns True if the local repository has a different commit ID to origin
        """
        if self.fetch_info is not None:
            origin_commit_id = self.fetch_info.commit
            local_commit_id = self.repo.head.commit
            updates_available = local_commit_id != origin_commit_id
        else:
            updates_available = False

        return updates_available

    def fetch_from_origin(self) -> Optional[FetchInfo]:
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
            branch_info = fetch_info[0]

        return branch_info

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
        Attempt to clone new repository if the supplied path is empty

        Paramters:
            path: Location of directory to initialise

        Raises:
            OSError If the supplied directory is not empty
        """
        if os.path.isdir(self.path) and len(os.listdir(self.path)) > 0:
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
