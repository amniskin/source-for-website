        (defn make-leaf [symbol weight]
          (list 'leaf symbol weight))
        (defn leaf? [object]
          (= (first object) 'leaf))
        (defn symbol-leaf [x] (second x))
        (defn weight-leaf [x] (nth x 2))
        (defn make-code-tree [left right]
          (list left
                right
                (concat (symbols left) (symbols right))
                (+ (weight left) (weight right))))
        (defn left-branch [tree] (first tree))

        (defn right-branch [tree] (second tree))
        (defn symbols [tree]
          (if (leaf? tree)
              (list (symbol-leaf tree))
              (nth tree 2)))
        (defn weight [tree]
          (if (leaf? tree)
              (weight-leaf tree)
              (nth tree 3)))
        (defn make-code-tree [left right]
          (list left
                right
                (concat (symbols left) (symbols right))
                (+ (weight left) (weight right))))
        (defn choose-branch [bit tree]
          (cond (= bit 0) (left-branch branch)
                (= bit 1) (right-branch branch)
                :else (throw (Exception. (str "Bad bit -- CHOOSE-BRANCH " bit)))))

        (defn decode [decode-bits huffman-tree]
          (loop [accu '()
                 bits decode-bits
                 tree huffman-tree]
            (if (empty? bits) (reverse accu)
              (let [next (choose-branch (first bits) tree)]
                (if (leaf? next)
                    (recur (conj accu (symbol-leaf next))
                           (rest bits)
                           huffman-tree)
                    (recur accu (rest bits) next))))))
        (defn adjoin-set [x set]
          (cond (empty? set) (list x)
                (< (weight x) (weight (first set)))
                  (conj set x)
                :else (conj (adjoin-set x (rest set))
                            (first set))))
          (defn adjoin-set [x set]
            (loop [accu   '()
                   things set]
              (cond (empty? things) (reverse (conj accu x))
                    (< (weight x) (weight (first things)))
                      (concat (reverse accu) (conj things x))
                    :else (recur (conj accu (first things)) (rest things)))))
