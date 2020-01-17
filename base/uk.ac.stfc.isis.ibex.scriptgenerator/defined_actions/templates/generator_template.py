from genie_python import genie as g

{% include inserted_config %}

def do_run():
    config = DoRun()
    {% for action in script_generator_actions -%}
    config.run(**{{ action.getAllActionParametersAsString() }})
    {% endfor -%}
