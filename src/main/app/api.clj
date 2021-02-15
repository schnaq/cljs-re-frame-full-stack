(ns app.api
  (:require [taoensso.timbre :as log]
            [compojure.core :refer [GET routes]]
            [org.httpkit.server :as server]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.util.http-response :refer [ok]])
  (:gen-class))

(defn- ping
  "Route to ping the API. Used in our monitoring system."
  [_]
  (ok {:text "🧙‍♂️"}))

(def ^:private app-routes
  "Common routes for all modes."
  (routes
   (GET "/ping" [] ping)))

(defonce current-server (atom nil))

(defn- stop-server []
  (when-not (nil? @current-server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@current-server :timeout 100)
    (reset! current-server nil)))

(defn -main
  "This is our main entry point for the REST API Server."
  [& _args]
  (reset! current-server
          (server/run-server
           (-> #'app-routes
               (wrap-restful-format :formats [:transit-json :transit-msgpack :json-kw :edn :msgpack-kw :yaml-kw :yaml-in-html])
               (wrap-defaults api-defaults))
           {:port 3000}))
  (log/info "Server started: http://localhost:3000/ping"))

(comment
  "Start the server from here"
  (-main)
  (stop-server)
  :end)
