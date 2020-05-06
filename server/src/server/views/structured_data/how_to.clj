(ns server.views.structured-data.how-to)

(defn supply
  [i]
  (when i
    {"@type" "HowToSupply"
     "name" i}))

(defn supplies
  [items]
  (when (not-empty items)
    {"supply" (->> items
                   (keep supply))}))

(defn tool
  [i]
  (when i
    {"@type" "HowToTool"
     "name" i}))

(defn tools
  [items]
  (when (not-empty items)
    {"tool" (->> items
                 (keep tool))}))

(defn step
  [i]
  (when i
    {"@type" "HowToStep"
     "text" i}))

(defn steps
  [items]
  (when (not-empty items)
    {"step" (->> items
                 (keep step))}))

(defn json-ld
  [{:keys [data-type] :as structured-data}]
  (merge
    {"@type" "HowTo"}
    (supplies (:supplies structured-data))
    (tools (:tools structured-data))
    (steps (:steps structured-data))))
