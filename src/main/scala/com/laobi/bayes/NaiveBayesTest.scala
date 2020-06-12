package com.laobi.bayes

import org.apache.spark.mllib.classification.NaiveBayes
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.{SparkConf, SparkContext}

object NaiveBayesTest {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    conf.setMaster("local[2]")
    conf.setAppName("bayes")
    val sc = new SparkContext(conf)

    val arr = Array[String](
      "0,0 0 0 0",
      "0,0 0 0 1",
      "1,1 0 0 0",
      "1,2 1 0 0",
      "1,2 2 1 0",
      "0,2 2 1 1",
      "1,1 2 1 1",
      "0,0 1 0 0",
      "1,0 2 1 0",
      "1,2 1 1 0",
      "1,0 1 1 1",
      "1,1 1 0 1",
      "1,1 0 1 0",
      "0,2 1 0 1"
    )

    val lp = MLUtils.loadLibSVMFile(sc, "/Users/byb/Downloads/part-00000")
    val splits = lp.randomSplit(Array(0.7, 0.3), seed = 51L)
    val training = splits(0)
    val test = splits(1)
    // 0.9971550497866287
    val model = NaiveBayes.train(training, 1.0, "multinomial")

//    model.save(sc, "/Users/byb/Downloads/bayes2")

    val predictionAndLabel= test.map(p => (model.predict(p.features),p.label))
    val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()

    println("accuracy  =>  " + accuracy)

    val tp = test.collect().head

    println("tp => " + tp)

    println(model.predict(tp.features))



    //读入数据
//    val data = sc.makeRDD(arr)
//    val parsedData = data.map { line =>
//      val parts = line.split(',')
//      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
//    }

    // 把数据的60%作为训练集，40%作为测试集.
//    val splits = parsedData.randomSplit(Array(0.6, 0.4), seed = 11L)
//    val training = splits(0)
//    val test = splits(1)
//
//    //获得训练模型,第一个参数为数据，第二个参数为平滑参数，默认为1，可改
//    val model = NaiveBayes.train(training,lambda = 1.0, modelType = "multinomial")
//
//    model.save(sc, "/Users/byb/Downloads/bayes")
//
//    val loadModel = NaiveBayesModel.load(sc, "/Users/byb/Downloads/bayes")
//
//
//
//    //对模型进行准确度分析
//    val predictionAndLabel= test.map(p => (model.predict(p.features),p.label))
//    val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()
//
//    println("accuracy-->"+accuracy)
//    println("Predictionof (0.0, 2.0, 0.0, 1.0):"+model.predict(Vectors.dense(0.0,0.0,0.0,0.0)))

  }
}
