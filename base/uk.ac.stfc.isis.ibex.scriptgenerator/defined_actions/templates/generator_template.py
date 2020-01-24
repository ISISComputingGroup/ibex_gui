{% include inserted_config %}

def runscript():
    config = DoRun()
    {% for action in script_generator_actions -%}
    config.run(**{{ action }})
    {% endfor -%}
