import unittest
from mock import patch
from script_definition_loader import get_script_definitions, unbundle_script_definitions_repository
from mock import Mock


class DefinitionsRepoTests(unittest.TestCase):
    @patch("script_definition_loader.Repo")
    @patch("script_definition_loader.Git")
    def test_GIVEN_uninitialised_directory_WHEN_repo_cloned_THEN_git_called_with_correct_arguments(self, git_clone, mock_repo):
        git_instance_clone = Mock()
        git_clone.return_value = git_instance_clone
        bundle_path = "path/to/bundle"
        repo_path = "path/to/repo"
        unbundle_script_definitions_repository(bundle_path, repo_path, "")
        git_instance_clone.clone.assert_called_with(bundle_path, repo_path)

    @patch("script_definition_loader.Repo")
    @patch("script_definition_loader.Git")
    def test_GIVEN_uninitialised_directory_WHEN_repo_cloned_THEN_new_repo_has_correct_remote_URL(self, git_clone, mock_repo):
        repo_instance_clone = Mock()
        mock_repo.return_value = repo_instance_clone
        bundle_path = "path/to/bundle"
        repo_path = "path/to/repo"
        upstream_url = "upstream_url.com"
        unbundle_script_definitions_repository(bundle_path, repo_path, upstream_url)
        repo_instance_clone.delete_remote.assert_called_with("origin")
        repo_instance_clone.create_remote.assert_called_with("origin", upstream_url)

