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

        # if not self._repo_already_exists():
        #     self._attempt_repo_init()

        # self.repo = Repo(self.path)

    def _repo_already_exists(self) -> bool:
        """
        Checks if there is a repository initialised at the supplied path with the correct url
        """
        definitions_repo = None
        try:
            definitions_repo = Repo(self.path)
        except (NoSuchPathError, InvalidGitRepositoryError):
            pass

        if definitions_repo is not None and definitions_repo.remotes['origin'] == self.remote_url:
            repo_exists = True
        else:
            repo_exists = False

        return repo_exists

    def pull_from_origin(self):
        origin = self.repo.remotes['origin']
        origin.pull()

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
        self.git.clone(self.bundle_path, self.path)
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
            raise OSError("Supplied directory is not empty, cannot make repository")

        try:
            print('aaaa')
            self.git.clone(self.remote_url, self.path)
            print('bbbb')
        except GitCommandError as err:
            print('cccc')
            print(err)
            self.clone_repo_from_bundle()


# try:
#     repo = Repo('...')
# except (git.exc.NoSuchPathError, git.exc.InvalidGitRepositoryError):
#     print("Path does not exist or is not initialised, cloning new repo")
#     repo = unbundle_script_definitions_repository

# if repo.remotes['origin'].url != url: # repository_initialised_correctly(repo, url):
#     try:
#         repo.remotes['origin'].pull()
#     except Exception as err:
#         script_definition_load_errors.append(err)
# else:
#     script_definition_load_errors.append("Script generator definitions not initialised correctly")

