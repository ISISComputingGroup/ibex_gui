import unittest
try:
    from unittest.mock import patch, Mock, MagicMock
except ImportError:
    from mock import patch, Mock, MagicMock
from git_utils import DefinitionsRepository, DEFAULT_REPO_PATH

from git.exc import GitCommandError


TEST_REPO_PATH = "path/to/repo"
TEST_BUNDLE_PATH = "path/to/bundle"
TEST_URL = "example.com"


class Branch:
    def __init__(self, name):
        self.name = name

class DefinitionsRepositoryTests(unittest.TestCase):
    def _raise_error_when_attempting_merge(_, abort=None):
        """
        Raises a git error if attempting a merge. Does nothing if abort is set to True.
        """
        if abort is True:
            pass
        else:
            raise GitCommandError(command='command', status='status')

    @patch("os.path.exists", return_value=True)
    @patch("git_utils.Git", return_value=Mock())
    @patch.object(DefinitionsRepository, "initialise_repo", return_value=MagicMock())
    def setUp(self, mock_repo, mock_git, _):
        self.mock_git = mock_git.return_value
        self.mock_repo = mock_repo.return_value
        self.definitions_repo = DefinitionsRepository(path=TEST_REPO_PATH, bundle_path=TEST_BUNDLE_PATH, remote_url=TEST_URL)

    def tearDown(self):
        # Ensure git clone mock is reset between tests
        self.mock_git.clone.reset_mock(return_value=True, side_effect=True)
        self.mock_repo.remotes["origin"].pull.reset_mock(return_value=True, side_effect=True)

    def test_GIVEN_uninitialised_directory_WHEN_repo_cloned_from_bundle_THEN_git_called_with_correct_arguments(self):
        bundle_path = "path/to/bundle"

        self.definitions_repo.clone_repo_from_bundle()

        self.mock_git.clone.assert_called_with(bundle_path, TEST_REPO_PATH)

    def test_GIVEN_origin_change_requested_THEN_git_commands_to_change_origin_called(self):
        self.definitions_repo._change_origin_url()
        self.mock_repo.delete_remote.assert_called_with("origin")
        self.mock_repo.create_remote.assert_called_with("origin", TEST_URL)

    @patch("git_utils.Repo")
    def test_GIVEN_uninitialised_directory_WHEN_repo_cloned_from_bundle_THEN_new_repo_has_correct_remote_URL(self, mock_repo):
        with patch.object(self.definitions_repo, "_change_origin_url") as mock_origin_change:
            self.definitions_repo.clone_repo_from_bundle()
            mock_origin_change.assert_called()

    @patch("git_utils.Repo")
    def test_GIVEN_repository_with_incorrect_url_WHEN_checking_that_repo_exists_THEN_return_repo_does_not_exist(self, mock_repo):
        repo_instance = mock_repo.return_value
        repo_instance.remotes["origin"].url = "different_remote_address.com"

        self.assertFalse(self.definitions_repo._repo_already_exists())

    @patch("git_utils.Repo")
    def test_GIVEN_repository_with_correct_url_WHEN_checking_that_repo_exists_THEN_return_repo_does_exist(self, mock_repo):
        repo_instance = mock_repo.return_value
        repo_instance.remotes["origin"].url = self.definitions_repo.remote_url

        self.assertTrue(self.definitions_repo._repo_already_exists())

    @patch("os.path.isdir", return_value=True)
    @patch("os.listdir", return_value=[])
    def test_GIVEN_remote_repository_reachable_WHEN_cloning_the_repository_THEN_clone_from_github_requested(self, mock_list, mock_isdir):
        self.definitions_repo._attempt_repo_init()
        self.mock_git.clone.assert_called_with(TEST_URL, TEST_REPO_PATH)

    def test_GIVEN_remote_repository_unreachable_WHEN_cloning_the_repository_THEN_clone_from_bundle(self):
        self.mock_git.clone = MagicMock(side_effect=GitCommandError(command="command", status="status"))

        with patch.object(self.definitions_repo, "clone_repo_from_bundle") as mock_clone:
            self.definitions_repo._attempt_repo_init()

            mock_clone.assert_called()

    @patch("git_utils.Repo")
    def test_GIVEN_repository_can_be_pulled_WHEN_repo_initialised_THEN_no_error_gets_logged(self, mock_repo):
        with patch.object(self.definitions_repo, "_append_error") as error_handler:
            with patch.object(self.definitions_repo, "_repo_already_exists", return_value=True):
                self.definitions_repo.initialise_repo()
                error_handler.assert_not_called()

    @patch("git_utils.Repo", side_effect=GitCommandError(command="Command", status="Status"))
    def test_GIVEN_repository_cannot_be_initialised_WHEN_repo_initialised_THEN_error_gets_logged(self, mock_repo):
        with patch.object(self.definitions_repo, "_append_error") as error_handler:
            with patch.object(self.definitions_repo, "_repo_already_exists", return_value=True):
                self.definitions_repo.initialise_repo()
                error_handler.assert_called()

    def test_GIVEN_origin_fetch_information_available_WHEN_local_and_origin_commits_different_THEN_updates_available_is_true(self):
        self.definitions_repo.fetch_info = Mock()
        self.definitions_repo.fetch_info.commit = 10
        self.mock_repo.head.commit = 11

        self.assertTrue(self.definitions_repo.updates_available())

    def test_GIVEN_origin_fetch_information_available_WHEN_local_and_origin_commits_equal_THEN_updates_available_is_false(self):
        self.definitions_repo.fetch_info = Mock()
        self.definitions_repo.fetch_info.commit = 10
        self.mock_repo.head.commit = 10

        self.assertFalse(self.definitions_repo.updates_available())

    def test_GIVEN_origin_fetch_info_not_available_WHEN_updates_available_requested_THEN_false(self):
        self.definitions_repo.fetch_info = None

        self.assertFalse(self.definitions_repo.updates_available())

    def test_GIVEN_correct_branch_in_fetch_info_WHEN_fetch_from_origin_requested_THEN_branch_info_returned(self):
        expected_branch = Branch("origin/master")
        self.mock_repo.remotes["origin"].fetch.return_value = [expected_branch, Branch("master")]
        self.assertIs(self.definitions_repo.fetch_from_origin(), expected_branch)

    def test_GIVEN_correct_branch_in_fetch_info_WHEN_fetch_from_origin_requested_THEN_None_returned(self):
        self.mock_repo.remotes["origin"].fetch.return_value = []
        self.assertIs(self.definitions_repo.fetch_from_origin(), None)

    def test_GIVEN_multiple_branches_with_correct_name_in_fetch_info_WHEN_fetch_from_origin_requested_THEN_first_branch_returned(self):
        branch = Branch("origin/master")
        self.mock_repo.remotes["origin"].fetch.return_value = [branch, Branch("origin/master")]
        self.assertIs(self.definitions_repo.fetch_from_origin(), branch)
        
    @patch("os.path.exists", return_value=False)
    @patch("git_utils.Git", return_value=Mock())
    @patch.object(DefinitionsRepository, "initialise_repo", return_value=MagicMock())
    def test_GIVEN_repo_path_does_not_exist_THEN_path_set_to_to_relative_directory_instead(self, mock_repo, mock_git, _):
        self.definitions_repo = DefinitionsRepository(path=TEST_REPO_PATH, bundle_path=TEST_BUNDLE_PATH, remote_url=TEST_URL)
        self.assertEqual(self.definitions_repo.path, DEFAULT_REPO_PATH)
        
    @patch("os.path.exists", return_value=True)
    @patch("git_utils.Git", return_value=Mock())
    @patch.object(DefinitionsRepository, "initialise_repo", return_value=MagicMock())
    def test_GIVEN_repo_path_does_exist_THEN_repo_path_is_correct(self, mock_repo, mock_git, _):
        self.definitions_repo = DefinitionsRepository(path=TEST_REPO_PATH, bundle_path=TEST_BUNDLE_PATH, remote_url=TEST_URL)
        self.assertEqual(self.definitions_repo.path, TEST_REPO_PATH)
