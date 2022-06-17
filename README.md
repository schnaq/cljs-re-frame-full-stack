# Full Stack Clojure / ClojureScript Project with re-frame

## Setup

First, install the dependencies with npm:

    yarn

Then, you can start the frontend service with:

    clj -M:frontend

You find the application at http://localhost:8700.

Start the backend API with this alias:

    clj -M:api

Find the backend server's documentation at: http://localhost:3000

## Using Calva

When you are using Calva, you can start frontend and backend in one REPL Session by using the jack-in command, then choosing `deps.edn + shadow-cljs` and `:frontend` as the preferred alias.

To start the backend, load `api.clj` into your repl and call the main-function.

### Demo (check also Shortcuts below)

video::resources/demo.mp4[Screencast showing how to connect to the REPL]

Better quality video is available at https://youtu.be/qFh9c_fNpjM[YouTube]

### Shortcut Advices

It is especially great if you have a REPL to be able to interact with the code directly from the editor. There are a few keyboard shortcuts for this, which you can
configure them as you like. These are my (Christian) recommendations:

- `C-c C-c` (Control C, Control C): Evaluates S-Expression (thus sends the
  current code from the cursor to the REPL).
- `C-c C-k`: Loads all the code from the file into the REPL.
- `C-c right`: Moves the closing parenthesis one expression further to the right.
- `C-c left`: Moves the closing parenthesis one expression back to the left.
- `C-c C-d`: If you have accidentally created an endless loop, cancel the last expression with this abbreviation.

To install the shortcuts, open the Keyboard Shortcuts in VSCode under Preferences. In the top right-hand corner you will find an icon with which you can open the configuration file.

There you can add the following entries.

```json
[
  {
    "key": "ctrl+c ctrl+c",
    "command": "calva.evaluateCurrentTopLevelForm",
    "when": "calva:connected"
  },
  {
    "key": "ctrl+c ctrl+d",
    "command": "calva.interruptAllEvaluations",
    "when": "calva:connected"
  },
  {
    "key": "ctrl+c ctrl+k",
    "command": "calva.loadFile",
    "when": "calva:connected"
  },
  {
    "key": "ctrl+c right",
    "command": "paredit.slurpSexpForward",
    "when": "calva:keybindingsEnabled && editorTextFocus && editorLangId == 'clojure' && editorLangId == 'clojure' && paredit:keyMap =~ /original|strict/"
  },
  {
    "key": "ctrl+c left",
    "command": "paredit.barfSexpForward",
    "when": "calva:keybindingsEnabled && editorTextFocus && editorLangId == 'clojure' && editorLangId == 'clojure' && paredit:keyMap =~ /original|strict/"
  }
]
```
