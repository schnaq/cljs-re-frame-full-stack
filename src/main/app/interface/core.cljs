(ns app.interface.core
  (:require [day8.re-frame.http-fx]
            [goog.dom :as gdom]
            [reagent.dom]))

(defn base []
  [:<>
   [:h1 "Hello"]
   [:p "My first page!"]])

;; -- Entry Point -------------------------------------------------------------

(defn render
  []
  (reagent.dom/render [base]
                      (gdom/getElement "app")))

(defn ^:dev/after-load clear-cache-and-render!
  []
  (render))

(defn init
  "Entrypoint into the application."
  []
  (render))