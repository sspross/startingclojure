(ns startingclojure.app
  (:use (compojure handler
                   [core :only (GET POST defroutes)]))
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]))

(def counter (atom 10000))

(def urls (atom {}))

(defn shorten
  [url]
  (let [id (swap! counter inc)
        id (Long/toString id 36)]
    (swap! urls assoc id url)
    id))

(defn home
  [request]
  (str @urls))

(defn redirect
  [id]
  (response/redirect (@urls id)))

(defroutes app
  (GET "/" [request] (home request))
  (GET "/:id" [id] (redirect id)))

(defn -main [port]
  (jetty/run-jetty #'app {:port (Integer. port) :join? false}))

;; Local
;;(def server (jetty/run-jetty #'app {:port 8080 :join? false}))

;; Hello World Heroku
;;(defn app [req]
;;  {:status 200
;;   :headers {"Content-Type" "text/plain"}
;;   :body "Hello, world"})
;;
;;(defn -main [port]
;;  (jetty/run-jetty app {:port (Integer. port) :join? false}))
