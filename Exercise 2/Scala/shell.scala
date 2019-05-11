spark2-shell --deploy-mode=client
val fibs20 = sc.parallelize(List( 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181))
val evens = fibs20.filter(_ % 2 == 0) // CORRECT: Checked with evens.collect().foreach(println)
val avg = fibs20.sum / fibs20.count()



val dataPath = "file://" + "/home/adbs/2019S/shared/diamonds.csv"