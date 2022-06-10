(ns app.interface.core
  (:require ["react-dom/client" :refer [createRoot]]
            [day8.re-frame.http-fx]
            [goog.dom :as gdom]
            [reagent.core :as r]))

(defn main []
  [:div.container
   [:h1 "Jatumba!"]
   [:p "My first page!"]])










;; -- Entry Point -------------------------------------------------------------

(defonce root (createRoot (gdom/getElement "app")))

(defn init
  []
  (.render root (r/as-element [main])))

(defn- ^:dev/after-load re-render
  "The `:dev/after-load` metadata causes this function to be called after
  shadow-cljs hot-reloads code. This function is called implicitly by its
  annotation."
  []
  (init))
