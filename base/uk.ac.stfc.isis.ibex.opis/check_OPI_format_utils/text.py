from check_OPI_format_utils.common import WIDGET_XPATH

# If a word contains any of the following, the whole word will be ignored
IGNORED_WORDS = ["$", "&", "#", '"', "'", "(", ")", "OPI", "PSU", "HW", "Hz", "LED", "A:", "B:", "C:", "D:", "CCD100",
                 "ISIS", "JJ", "X-Ray", "LTC"]

CONTAINER_NAME_XPATH = "//{groupingContainer}/name".format(groupingContainer=WIDGET_XPATH.format("groupingContainer"))

LABEL_IN_CONTAINER_XPATH = "//{groupingContainer}/{label}/text"\
    .format(groupingContainer=WIDGET_XPATH.format("groupingContainer"), label=WIDGET_XPATH.format("Label"))

# Label widget xpath should not end with a closing square bracket
LABEL_OUTSIDE_CONTAINER_XPATH = "//{label} and not(ancestor::{groupingContainer})]/text"\
    .format(groupingContainer=WIDGET_XPATH.format("groupingContainer"), label=WIDGET_XPATH.format("Label")[:-1])


# Ignore words below this length
SHORT_WORD_LIM = 3


def _generate_valid_labels(root, xpath):
    for label in root.xpath(xpath):
        text = label.text
        if text is not None and len(text) > 0 and not any(s in text for s in IGNORED_WORDS):
            yield label


def _filter_out_good_punctuation(labels):
    for label in labels:
        text = label.text
        last_character_is_colon = text[-1:] == ":"
        ends_in_ellipsis = text.endswith("...")
        text_is_numeric = text.isdigit()

        if not any((last_character_is_colon, ends_in_ellipsis, text_is_numeric)):
            yield (label.sourceline, label.text)


def check_label_punctuation(root):
    """
    Checks that the labels in an OPI end with the correct punctuation.
    :param root (etree): the root xml element
    :return: A list of tuples (linenumber, text) where the correct punctuation was not present.
    """

    return list(_filter_out_good_punctuation(_generate_valid_labels(root, LABEL_IN_CONTAINER_XPATH)))


def check_container_names(root):
    return list(_filter_out_good_case(_generate_valid_labels(root, CONTAINER_NAME_XPATH), True))


def check_label_case_outside_containers(root):
    return list(_filter_out_good_case(_filter_manager_mode(_generate_valid_labels(root, LABEL_OUTSIDE_CONTAINER_XPATH)), True))


def check_label_case_inside_containers(root):
    """
    Checks the capitalisation of labels
    :return: A list of tuples (linenumber, text) where the correct punctuation was not present.
    """
    return list(_filter_out_good_case(_generate_valid_labels(root, LABEL_IN_CONTAINER_XPATH), False))


def _filter_manager_mode(labels):
    for label in labels:
        if "manager mode" not in label.text.lower():
            yield label


def _filter_out_good_case(labels, title_case):
    for label in labels:
        # Handle special case of first word (which should be capitalised regardless of length)
        words = label.text.split()
        first_word = words.pop(0)
        if not first_word.title() == first_word or not is_case_correct(title_case, words):
            yield (label.sourceline, label.text)


def is_case_correct(title_case, words):
    for word in words:
        # Ignore words less than 4 characters as these are probably prepositions
        is_long_word = len(word) > SHORT_WORD_LIM
        if is_long_word:
            if title_case and word.title() != word:
                return False
            elif not title_case and word.lower() != word:
                return False
    return True
