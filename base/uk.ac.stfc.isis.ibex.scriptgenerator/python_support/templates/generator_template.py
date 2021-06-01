# pylint: skip-file

{% include inserted_script_definition %}

def runscript():
    script_definition = DoRun()
    if hasattr(script_definition, "global_params_definition"):
        script_definition.global_params = dict(zip(script_definition.global_params_definition.keys(), {{ global_params }}))
    {% for action in script_generator_actions -%}
    script_definition.run(**{{ action }})
    {% endfor %}


value = {{ hexed_value }}