import unittest
from mock import patch
import git_utils
from git_utils import DefinitionsRepository

from mock import Mock
from git.exc import GitCommandError
import tempfile


TEST_REPO_PATH = "path/to/repo"
TEST_BUNDLE_PATH = "path/to/bundle"


class DefinitionsRepositoryTests(unittest.TestCase):
    @patch("git_utils.Git", return_value=Mock())
    def setUp(self, mock_git):
        self.mock_git = mock_git.return_value
        self.definitions_repo = DefinitionsRepository(directory_path=TEST_REPO_PATH, bundle_path=TEST_BUNDLE_PATH)

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
        repo_instance.create_remote.assert_called_with("origin", git_utils.REMOTE_URL)

    def test_GIVEN_remote_repository_reachable_WHEN_cloning_the_repository_THEN_clone_from_github_requested(self):
        self.definitions_repo._attempt_repo_init()
        self.mock_git.clone.assert_called_with(git_utils.REMOTE_URL, TEST_REPO_PATH)

    def raise_hell(self, x, y):
        print('I have been called')
        raise RuntimeError

    # @patch("git_utils.Git")
    # @patch("git_utils.DefinitionsRepository.clone_repo_from_bundle")
    # def test_GIVEN_remote_repository_unreachable_WHEN_cloning_the_repository_THEN_clone_from_bundle(self, mock_clone, mock_git):
    #     repo_instance = Mock()
    #     self.mock_git.clone.side_effect = GitCommandError(command='command', status='status')
    #     mock_git.return_value = repo_instance
        
    #     self.definitions_repo._attempt_repo_init()

    #     # with patch.object(self.definitions_repo.git, "clone") as mock:
    #     #     mock.return_value = GitCommandError
    #     #     self.definitions_repo._attempt_repo_init()
    #     #     mock.assert_called()
        
    #     mock_clone.assert_called()

    #     del self.mock_git.clone.side_effect

    # @patch("script_definition_loader.Repo")
    # @patch("script_definition_loader.clone_repo_from_bundle")
    # def test_GIVEN_uninitialised_repository_WHEN_web_clone_fails_THEN_attempt_clone_from_bundle(self, mock_unbundle_clone, mock_repo):
    #     # Make an instance of Repo which raises error when clone is attempted
    #     repo_instance = Mock()
    #     repo_instance.clone = GitCommandError
    #     mock_repo.return_value = repo_instance

    #     with tempfile.TemporaryDirectory() as tmpdirname:
    #         clone_definitions_repo(tmpdirname)

    #     mock_unbundle_clone.assert_called()
