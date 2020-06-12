import unittest
from mock import patch
from git_utils import DefinitionsRepository

from mock import Mock
from git.exc import GitCommandError


TEST_REPO_PATH = "path/to/repo"
TEST_BUNDLE_PATH = "path/to/bundle"
TEST_URL = "example.com"


class DefinitionsRepositoryTests(unittest.TestCase):
    @patch("git_utils.Git", return_value=Mock())
    def setUp(self, mock_git):
        self.mock_git = mock_git.return_value
        self.definitions_repo = DefinitionsRepository(directory_path=TEST_REPO_PATH, bundle_path=TEST_BUNDLE_PATH, remote_url=TEST_URL)

    def tearDown(self):
        # Ensure git clone mock is reset between tests
        self.mock_git.clone.reset_mock(return_value=True, side_effect=True)

    @patch("git_utils.Repo")
    def test_GIVEN_uninitialised_directory_WHEN_repo_cloned_from_bundle_THEN_git_called_with_correct_arguments(self, _):
        bundle_path = "path/to/bundle"

        self.definitions_repo.clone_repo_from_bundle()

        self.mock_git.clone.assert_called_with(bundle_path, TEST_REPO_PATH)

    @patch("git_utils.Repo")
    def test_GIVEN_uninitialised_directory_WHEN_repo_cloned_from_bundle_THEN_new_repo_has_correct_remote_URL(self, mock_repo):
        repo_instance = mock_repo.return_value

        self.definitions_repo.clone_repo_from_bundle()
        repo_instance.delete_remote.assert_called_with("origin")
        repo_instance.create_remote.assert_called_with("origin", TEST_URL)

    @patch("git_utils.Repo")
    def test_GIVEN_repository_with_incorrect_url_WHEN_checking_that_repo_exists_THEN_return_repo_does_not_exist(self, mock_repo):
        repo_instance = mock_repo.return_value
        repo_instance.remotes['origin'].url = "different_remote_address.com"

        self.assertFalse(self.definitions_repo._repo_already_exists())

    @patch("git_utils.Repo")
    def test_GIVEN_repository_with_correct_url_WHEN_checking_that_repo_exists_THEN_return_repo_does_exist(self, mock_repo):
        repo_instance = mock_repo.return_value
        repo_instance.remotes['origin'].url = self.definitions_repo.remote_url

        self.assertTrue(self.definitions_repo._repo_already_exists())

    def test_GIVEN_remote_repository_reachable_WHEN_cloning_the_repository_THEN_clone_from_github_requested(self):
        self.definitions_repo._attempt_repo_init()
        self.mock_git.clone.assert_called_with(TEST_URL, TEST_REPO_PATH)

    def test_GIVEN_remote_repository_unreachable_WHEN_cloning_the_repository_THEN_clone_from_bundle(self):
        self.mock_git.clone.side_effect = GitCommandError(command='command', status='status')

        with patch.object(self.definitions_repo, "clone_repo_from_bundle") as mock_clone:
            self.definitions_repo._attempt_repo_init()

            mock_clone.assert_called()

    @patch("os.makedirs")
    @patch("os.path.isdir")
    def test_GIVEN_directory_which_does_not_exist_WHEN_repository_initialise_called_THEN_makedirs_called_to_create_folders(self, mock_isdir, mock_makedirs):
        mock_isdir.return_value = False
        self.definitions_repo._attempt_repo_init()
        mock_makedirs.assert_called_with(TEST_REPO_PATH, exist_ok=True)

    @patch("git_utils.Repo")
    def test_GIVEN_repository_exists_WHEN_pull_requested_THEN_repository_gets_pulled(self, mock_repo):
        with patch.object(self.definitions_repo, "_repo_already_exists", return_value=True):
            origin_instance = mock_repo.return_value.remotes['origin']

            self.definitions_repo._pull_from_origin(mock_repo.return_value)
            origin_instance.pull.assert_called()

    @patch("git_utils.Repo")
    def test_GIVEN_repository_which_has_unpushed_changes_WHEN_pull_attempted_THEN_merge_is_aborted(self, mock_repo):
        with patch.object(self.definitions_repo, "_repo_already_exists", return_value=True):
            repo_instance = mock_repo.return_value
            repo_instance.remotes['origin'].pull.side_effect = GitCommandError(command='command', status='status')

            self.definitions_repo._pull_from_origin(repo_instance)

            repo_instance.git.merge.assert_called_with(abort=True)

    @patch("git_utils.Repo")
    def test_GIVEN_repository_cannot_be_pulled_WHEN_repo_initialised_THEN_error_gets_logged(self, _):
        with patch.object(self.definitions_repo, "_pull_from_origin") as mock_pull:
            mock_pull.side_effect = GitCommandError(command="command", status="status")

            with patch.object(self.definitions_repo, "_append_error") as error_handler:
                with patch.object(self.definitions_repo, "_repo_already_exists", return_value=True):
                    self.definitions_repo.initialise_and_pull()
                    error_handler.assert_called()

    @patch("git_utils.Repo")
    def test_GIVEN_repository_can_be_pulled_WHEN_repo_initialised_THEN_no_error_gets_logged(self, _):
        with patch.object(self.definitions_repo, "_pull_from_origin"):
            with patch.object(self.definitions_repo, "_append_error") as error_handler:
                with patch.object(self.definitions_repo, "_repo_already_exists", return_value=True):
                    self.definitions_repo.initialise_and_pull()
                    error_handler.assert_not_called()
