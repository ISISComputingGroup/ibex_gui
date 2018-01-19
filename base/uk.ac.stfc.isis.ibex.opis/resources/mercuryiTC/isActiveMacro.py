def is_active_macro(value):
    if value is None or len(value) == 0:
        return False  # Empty macro
    elif len(value) >= 3 and value[:2] == "$(" and value[-1] == ")":
        return False  # Unexpanded macro
    else:
        return True
