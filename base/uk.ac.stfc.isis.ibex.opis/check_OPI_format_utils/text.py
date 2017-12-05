from check_OPI_format_utils.common import WIDGET_XPATH

# If a word contains any of the following, the whole word will be ignored
IGNORED_WORDS = ["$", "&", "#", '"', "'", "(", ")", "OPI", "PSU", "HW", "Hz", "LED", "A:", "B:", "C:", "D:", "CCD100",
                 "ISIS", "JJ", "X-Ray", "LTC", "IP", "PID"]

CONTAINER_NAME_XPATH = "//{groupingContainer}/name".format(groupingContainer=WIDGET_XPATH.format("groupingContainer"))

LABEL_IN_CONTAINER_XPATH = "//{groupingContainer}/{label}/text"\
    .format(groupingContainer=WIDGET_XPATH.format("groupingContainer"), label=WIDGET_XPATH.format("Label"))

# Label widget xpath should not end with a closing square bracket
LABEL_OUTSIDE_CONTAINER_XPATH = "//{label} and not(ancestor::{groupingContainer})]/text"\
    .format(groupingContainer=WIDGET_XPATH.format("groupingContainer"), label=WIDGET_XPATH.format("Label")[:-1])


# Ignore words below this length
SHORT_WORD_LIM = 3


def _generate_valid_labels(root, xpath):
    """
    Generator that returns any labels that contain text and do not contain ignored words.
    Args:
        root (etree): The root of the xml to search.
        xpath (str): The xpath string for what labels to search for.
    Yields:
        node: A label node that matches the above conditions
    """
    for label in root.xpath(xpath):
        text = label.text
        if text is not None and len(text) > 0 and not any(s in text for s in IGNORED_WORDS):
            yield label


def _filter_out_good_punctuation(labels):
    """
    Generator that filters out any labels that end in a colon or ellipsis or are purely digits.
    Args:
        labels (node generator): The labels to filter
    Yields:
        node: A label node that has incorrect punctuation
    """
    for label in labels:
        text = label.text
        last_character_is_colon = text[-1:] == ":"
        ends_in_ellipsis = text.endswith("...")
        text_is_numeric = text.isdigit()

        if not any((last_character_is_colon, ends_in_ellipsis, text_is_numeric)):
            yield (label.sourceline, label.text)


def _filter_manager_mode(labels):
    """
    Generator that filters out any labels that contain the text 'manager mode'.
    Args:
        labels (node generator): The labels to filter
    Yields:
        node: A label node that does not contain manager mode
    """
    for label in labels:
        if "manager mode" not in label.text.lower():
            yield label


def _filter_out_good_case(labels, title_case):
    """
    Generator that filters out any labels that match the given case.
    Args:
        labels (node generator): The labels to filter
        title_case (bool): True if filtering out title case, false if filtering sentence case
    Yields:
        node: A label node that does not conform to the given case.
    """
    for label in labels:
        # Handle special case of first word (which should be capitalised regardless of length)
        words = label.text.split()
        first_word = words.pop(0)
        if not first_word.title() == first_word or not _is_case_correct(title_case, words):
            yield (label.sourceline, label.text)


def _is_case_correct(title_case, words):
    """
    Checks that the given list of words matches the given case type.
    Args:
        title_case (bool): True if filtering out title case, false if filtering sentence case
        words (list of str): The words to check the case of
    Returns:
        bool: True if the case of the words is correct, false if not.
    """
    for word in words:
        # Ignore words less than 4 characters as these are probably prepositions
        is_long_word = len(word) > SHORT_WORD_LIM
        if is_long_word:
            if title_case and word.title() != word:
                return False
            elif not title_case and word.lower() != word:
                return False
    return True


def check_label_punctuation(root):
    """
    Checks the xml for any labels that do not end in a colon or ellipsis or are purely digits.
    Args:
        root (etree): The root of the xml to search.
    Returns:
        list: A list of tuples containing the line number and text of the widgets that do not match the criteria
    """
    return list(_filter_out_good_punctuation(_generate_valid_labels(root, LABEL_IN_CONTAINER_XPATH)))


def check_container_names(root):
    """
    Checks the xml for any container names that are not title case.
    Args:
        root (etree): The root of the xml to search.
    Returns:
        list: A list of tuples containing the line number and text of the widgets that do not match the criteria
    """
    return list(_filter_out_good_case(_generate_valid_labels(root, CONTAINER_NAME_XPATH), True))


def check_label_case_outside_containers(root):
    """
    Checks the xml for any labels that are outside containers and are not title case.
    Args:
        root (etree): The root of the xml to search.
    Returns:
        list: A list of tuples containing the line number and text of the widgets that do not match the criteria
    """
    return list(_filter_out_good_case(_filter_manager_mode(_generate_valid_labels(root, LABEL_OUTSIDE_CONTAINER_XPATH)), True))


def check_label_case_inside_containers(root):
    """
    Checks the xml for any labels that are inside containers and are not sentence case.
    Args:
        root (etree): The root of the xml to search.
    Returns:
        list: A list of tuples containing the line number and text of the widgets that do not match the criteria
    """
    return list(_filter_out_good_case(_generate_valid_labels(root, LABEL_IN_CONTAINER_XPATH), False))
