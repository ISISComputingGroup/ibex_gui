import unittest
from mock import patch
from script_definition_loader import (get_script_definitions,
                                      clone_repo_from_bundle,
                                      update_script_definitions,
                                      clone_definitions_repo)
from mock import Mock
from git.exc import GitCommandError
import tempfile


class DefinitionsRepoTests(unittest.TestCase):
    @patch("script_definition_loader.Repo")
    @patch("script_definition_loader.Git")
    def test_GIVEN_uninitialised_directory_WHEN_repo_cloned_from_bundle_THEN_git_called_with_correct_arguments(self, mock_git, mock_repo):
        git_instance_clone = Mock()
        mock_git.return_value = git_instance_clone
        bundle_path = "path/to/bundle"
        repo_path = "path/to/repo"
        clone_repo_from_bundle(repo_path, "", bundle_path)
        git_instance_clone.clone.assert_called_with(bundle_path, repo_path)

    @patch("script_definition_loader.Repo")
    @patch("script_definition_loader.Git")
    def test_GIVEN_uninitialised_directory_WHEN_repo_cloned_from_bundle_THEN_new_repo_has_correct_remote_URL(self, mock_git, mock_repo):
        repo_instance_clone = Mock()
        mock_repo.return_value = repo_instance_clone
        bundle_path = "path/to/bundle"
        repo_path = "path/to/repo"
        upstream_url = "upstream_url.com"
        clone_repo_from_bundle(repo_path, upstream_url, bundle_path)
        repo_instance_clone.delete_remote.assert_called_with("origin")
        repo_instance_clone.create_remote.assert_called_with("origin", upstream_url)

    @patch("script_definition_loader.clone_definitions_repo")
    def test_GIVEN_directory_supplied_does_not_exist_WHEN_script_generator_starting_up_THEN_attempt_to_clone_repository(self, mock_clone):
        repo_path = 'path/does/not/exist'
        update_script_definitions(repo_path)
        mock_clone.assert_called()

    @patch("script_definition_loader.Repo")
    @patch("script_definition_loader.clone_repo_from_bundle")
    def test_GIVEN_uninitialised_repository_WHEN_web_clone_fails_THEN_attempt_clone_from_bundle(self, mock_unbundle_clone, mock_repo):
        # Make an instance of Repo which raises error when clone is attempted
        repo_instance = Mock()
        repo_instance.clone = GitCommandError
        mock_repo.return_value = repo_instance

        with tempfile.TemporaryDirectory() as tmpdirname:
            clone_definitions_repo(tmpdirname)

        mock_unbundle_clone.assert_called()
