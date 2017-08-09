### Description of work

*Add your own description here*

### To test

*Which ticket does this PR fix?*

### Acceptance criteria

*List the acceptance criteria for the PR*

---

#### Code Review

- [ ] Is the code of an acceptable quality?
- [ ] Does the code conform to the [coding standards](https://github.com/ISISComputingGroup/ibex_developers_manual/wiki/GUI-Coding-Conventions)? Is it well structured with small focussed classes/methods/functions?
- [ ] Have no new checkstyle warnings been introduced? Check via Jenkins
- [ ] Are there unit tests in place? Are the unit tests small and test the a class in isolation?
- [ ] Are there automated [system tests](https://github.com/ISISComputingGroup/ibex_developers_manual/wiki/System-Testing-with-RCPTT) in place? Do they test a minimal set of functionality and leave the gui as close as possible to its original state?
- [ ] Has the [manual system tests spreadsheet](https://github.com/ISISComputingGroup/ibex_developers_manual/wiki/Manual-system-tests) been updated?
    - Manual system tests for the functionality should be added if there are no automated tests
    - Manual system tests can be removed from the template if they are covered by suitable automated tests
- [ ] Did any existing system test break as a result of the current changes? 
- [ ] Have the changes been documented in the [release notes](https://github.com/ISISComputingGroup/IBEX/wiki/ReleaseNotes_Dev). If so, do they describe the changes appropriately?
- [ ] If an OPI has been modified, does it conform to the [style guidelines](https://github.com/ISISComputingGroup/ibex_developers_manual/wiki/OPI-Creation)? There is a script called `check_opi_format.py` to help with this.

### Functional Tests

- [ ] Do changes function as described? Add comments below that describe the tests performed.
- [ ] How do the changes handle unexpected situations, e.g. bad input?
- [ ] Has developer documentation been updated if required?
- [ ] Have any new plugins been added to the appropriate feature.xml? You can check if this is needed by validating plugins as per method at  https://github.com/ISISComputingGroup/ibex_developers_manual/wiki/Common-Eclipse-Issues

### Final Steps
- [ ] Reviewer has moved the [release notes](https://github.com/ISISComputingGroup/IBEX/wiki/ReleaseNotes_Dev) entry for this ticket in the "Changes merged into master" section

