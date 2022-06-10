(ns app.api
  (:require [app.config :as config]
            [expound.alpha :as expound]
            [mount.core :as mount :refer [defstate]]
            [muuntaja.core :as m]
            [org.httpkit.server :as server]
            [reitit.coercion.spec]
            [reitit.dev.pretty :as pretty]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.spec :as rrs]
            [reitit.spec :as rs]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.http-response :refer [ok]]
            [taoensso.timbre :as log])
  (:gen-class))

(defn- wizard
  "Route to ping the API. Used in our monitoring system."
  [_]
  (ok {:wizard "🧙‍♂️"}))

(defn- reveal-information [request]
  (ok {:headers (:headers request)
       :identity (:identity request)}))

(def ^:private api-routes
  [["/debug" {:swagger {:tags ["debug"]}}
    ["" {:name :api/debug
         :get {:handler reveal-information}
         :post {:handler reveal-information}}]]
   ["/wizard" {:get wizard}]])






;; ----------------------------------------------------------------------------

(defn- router
  "Create a router with all routes. Configures swagger for documentation."
  []
  (ring/router
   [api-routes
    ["/swagger.json"
     {:get {:no-doc true
            :swagger {:info {:title "API"
                             :basePath "/"
                             :version "1.0.0"}}
            :handler (swagger/create-swagger-handler)}}]]
   {:exception pretty/exception
    :validate rrs/validate
    ::rs/explain expound/expound-str
    :data {:coercion reitit.coercion.spec/coercion
           :muuntaja m/instance
           :middleware [swagger/swagger-feature
                        parameters/parameters-middleware ;; query-params & form-params
                        muuntaja/format-middleware
                        coercion/coerce-response-middleware ;; coercing response bodies
                        coercion/coerce-request-middleware ;; coercing request parameters
                        multipart/multipart-middleware]}}))

(defn app
  []
  (ring/ring-handler
   (router)
   (ring/routes
    (swagger-ui/create-swagger-ui-handler
     {:path "/"
      :config {:validatorUrl nil
               :operationsSorter "alpha"}})
    (ring/redirect-trailing-slash-handler {:method :strip})
    (ring/create-default-handler))))

(def allowed-http-verbs
  #{:get :put :post :delete :options})

(defstate api
  :start
  (let [origins #".*"]
    (log/info (format "Allowed Origins: %s" origins))
    (log/info (format "Find the backend with swagger documentation at %s" config/api-location))
    (server/run-server
     (wrap-cors (app)
                :access-control-allow-origin origins
                :access-control-allow-methods allowed-http-verbs)
     {:port config/api-port}))
  :stop (when api (api :timeout 1000)))

(defn -main
  "This is our main entry point for the REST API Server."
  [& _args]
  (log/info (mount/start)))

(comment
  "Start the server from here"
  (-main)
  (mount/start)
  (mount/stop)
  :end)
