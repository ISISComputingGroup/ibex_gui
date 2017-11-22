from common import WIDGET_XPATH

# If a word contains any of the following, the whole word will be ignored
IGNORED_WORDS = ["$", "&", "#", '"', "'", "(", ")", "OPI", "PSU", "HW", "Hz", "LED", "A:", "B:", "C:", "D:", "CCD100",
                 "ISIS", "JJ", "X-Ray", "LTC"]


LABEL_IN_CONTAINER_XPATH = "//{groupingContainer}/{label}/text"\
    .format(groupingContainer=WIDGET_XPATH.format("groupingContainer"), label=WIDGET_XPATH.format("Label"))

# Label widget xpath should not end with a closing square bracket
LABEL_OUTSIDE_CONTAINER_XPATH = "//{label} and not(ancestor::{groupingContainer})]/text"\
    .format(groupingContainer=WIDGET_XPATH.format("groupingContainer"), label=WIDGET_XPATH.format("Label")[:-1])


# Ignore words below this length
SHORT_WORD_LIM = 3


def generate_labels(root, xpath):
    for label in root.xpath(xpath):
        text = label.text
        if text is not None and len(text) > 0:
            yield label

def _check_label_punctuation(generator):
    for label in generator:
        text = label.text
        last_character_is_colon = text[-1:] == ":"
        ends_in_ellipsis = text.endswith("...")
        text_is_numeric = text.isdigit()
        is_ignored = any(s in text for s in IGNORED_WORDS)

        if not any((last_character_is_colon, ends_in_ellipsis, text_is_numeric, is_ignored)):
            yield (label.sourceline, label.text)


def check_label_punctuation(root):
    """
    Checks that the labels in an OPI end with the correct punctuation.
    :param root (etree): the root xml element
    :return: A list of tuples (linenumber, text) where the correct punctuation was not present.
    """

    return list(_check_label_punctuation(generate_labels(root, LABEL_IN_CONTAINER_XPATH)))


def check_title_case(root):
    return list(check_case(filter_manager_mode(generate_labels(root, LABEL_OUTSIDE_CONTAINER_XPATH)), True))


def check_sentence_case(root):
    """
    Checks the capitalisation of labels
    :return: A list of tuples (linenumber, text) where the correct punctuation was not present.
    """
    return list(check_case(generate_labels(root, LABEL_IN_CONTAINER_XPATH), False))


def filter_manager_mode(labels):
    for label in labels:
        if not "manager mode" in label.text.lower():
            yield label


def check_case(labels, title_case):
    for label in labels:
        # Handle special case of first word (which should be capitalised regardless of length)
        words = label.text.split()
        first_word = words.pop(0)
        if (not first_word.title() == first_word and not any(s in first_word for s in IGNORED_WORDS)) \
                or not is_case_correct(title_case, words):
            yield label


def is_case_correct(title_case, words):
    for word in words:
        # Ignore words less than 4 characters as these are probably prepositions
        is_long_word = len(word) > SHORT_WORD_LIM
        is_ignored = any(s in word for s in IGNORED_WORDS)
        if is_long_word and not is_ignored:
            if title_case and word.title() != word:
                return False
            elif not title_case and word.lower() != word:
                return False
    return True

