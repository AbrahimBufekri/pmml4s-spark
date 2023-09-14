/* Copyright (c) 2017-2022 AutoDeploy AI
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. */
package org.pmml4s.spark

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.flatspec.AnyFlatSpec

/** Iris.csv is from: http://dmg.org/pmml/pmml_examples/Iris.csv single_iris_dectree.xml is from:
  * http://dmg.org/pmml/pmml_examples/KNIME_PMML_4.1_Examples/single_iris_dectree.xml
  */
class ScoreModelTest extends AnyFlatSpec {

  implicit val doubleEquality: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(0.000001)

  val spark: SparkSession = SparkSession.builder.master("local[*]").getOrCreate()

  val df: DataFrame = spark.read
    .format("csv")
    .options(Map("header" -> "true", "inferSchema" -> "true"))
    .load("src/test/resources/data/Iris.csv")
  df.show

  it should "Score a model in Spark with default settings" in {
    val model = ScoreModel.fromFile("src/test/resources/models/single_iris_dectree.xml")
    val scoreDf = model.transform(df)
    assert(scoreDf.schema.size === 11)
    scoreDf.printSchema()
    scoreDf.show
  }

  it should "Score a model in Spark with prependInputs=false" in {
    val model = ScoreModel.fromFile("src/test/resources/models/single_iris_dectree.xml")
    val scoreDf = model.setPrependInputs(false).transform(df)
    assert(scoreDf.schema.size === 6)
    assert(scoreDf.schema.fields(0).name === "predicted_class")
    scoreDf.printSchema()
    scoreDf.show
  }

  it should "Score a model in Spark with prependInputs=false and predictionCol=prediction" in {
    val model = ScoreModel.fromFile("src/test/resources/models/single_iris_dectree.xml")
    val scoreDf = model.setPrependInputs(false).setPredictionCol("prediction").transform(df)
    assert(scoreDf.schema.size === 6)
    assert(scoreDf.schema.fields(0).name === "prediction")
    scoreDf.printSchema()
    scoreDf.show
  }

  it should "Score a model with output in Spark with default settings" in {
    val model = ScoreModel.fromFile("src/test/resources/models/single_iris_dectree_output.xml")
    val scoreDf = model.setPrependInputs(false).transform(df)
    assert(scoreDf.schema.size === 1)
    assert(scoreDf.schema.fields(0).name === "prediction")
    scoreDf.printSchema()
    scoreDf.show
  }

  it should "Score a model with output in Spark with supplementOutput=true" in {
    val model = ScoreModel.fromFile("src/test/resources/models/single_iris_dectree_output.xml")
    val scoreDf = model.setPrependInputs(false).setSupplementOutput(true).transform(df)
    assert(scoreDf.schema.size === 6)
    assert(scoreDf.schema.fields(0).name === "prediction")
    scoreDf.printSchema()
    scoreDf.show
  }

}
