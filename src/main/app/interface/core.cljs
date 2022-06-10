(ns app.interface.core
  (:require ["react-dom/client" :refer [createRoot]]
            [ajax.core :as ajax]
            [app.config :as config]
            [day8.re-frame.http-fx]
            [goog.dom :as gdom]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [taoensso.timbre :as log]))

(defn- main
  "Main view for the application."
  []
  (let [wizard @(rf/subscribe [:wizard])]
    [:div.container
     [:h1 "Welcome"]
     [:p "My first page!"]
     [:button.btn.btn-outline-primary {:on-click #(rf/dispatch [:wizard/get])}
      "Query Wizard from Backend"]
     (when wizard [:p.display-1.pt-3 wizard])]))


;; -----------------------------------------------------------------------------
;; Events and Subscriptions to query the backend and store the result in the
;; app-state.

(rf/reg-event-fx
 :wizard/get
 (fn [_ _]
   {:fx [[:http-xhrio {:method :get
                       :uri (str config/api-location "/wizard")
                       :format (ajax/transit-request-format)
                       :response-format (ajax/transit-response-format)
                       :on-success [:wizard.get/success]
                       :on-failure [:wizard.get/error]}]]}))

(rf/reg-event-db
 :wizard.get/success
 (fn [db [_ response]]
   (assoc db :wizard (:wizard response))))

(rf/reg-event-fx
 :wizard.get/error
 (fn [_ [_ error]]
   {:fx [[:log/error (str "Could not query the wizard. Did you forget to start the api? " error)]]}))

(rf/reg-fx
 :log/error
 (fn [message]
   (log/error message)))

(rf/reg-sub
 :wizard
 (fn [db _]
   (:wizard db)))





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
  (rf/clear-subscription-cache!)
  (init))
