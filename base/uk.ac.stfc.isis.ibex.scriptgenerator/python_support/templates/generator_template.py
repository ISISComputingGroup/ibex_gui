# pylint: skip-file

{% include inserted_script_definition %}

def runscript():
    script_definition = DoRun()
    {% for action in script_generator_actions -%}
    script_definition.run(**{{ action }})
    {% endfor %}


value = {{ hexed_value }}