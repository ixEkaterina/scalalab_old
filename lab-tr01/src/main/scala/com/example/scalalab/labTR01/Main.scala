package com.example.scalalab.labTR01
import org.apache.spark.sql.types.{StringType, DoubleType, DateType, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}

object Main {

  def main(args: Array[String]):Unit = {
    val spark = SparkSession.builder.master ("local[*]").appName ("SparklabTR01").getOrCreate ()
    val params: Map[String, String] = extract(args)

    println ("Hello, I'm TR01!")
    val path = "C:\\Users\\rusta\\Documents\\scala_neoflex\\scalalab\\dataCSVAgreements.csv"
    val csv = readCSV(spark, path)
    csv.show(3)

    spark.stop ()
  }

  def extract(args: Array[String]): Map[String, String] = {
   args
     .map(_.split("="))
     .map( t => ( t(0), t(1) ))
     .toMap
  }

  def selectFromDB(spark: SparkSession, params: Map[String, String], tableOrQuery: String): DataFrame = {

    spark
      .read
      .format("jdbc")
      .option("url", params.getOrElse("jdbc_uri", throw new IllegalArgumentException("jdbc_uri not found")))
      .option("dbtable", tableOrQuery)
      .option("user", "username")
      .option("password", "password")
      .load()

  }

  def readCSV(spark: SparkSession, path: String): DataFrame = {
    val schema = new StructType()
      .add("name",StringType,false)
      .add("birth_date",DateType,false)
      .add("inn",StringType,false)
      .add("passport",StringType,false)
      .add("branch",StringType,false)
      .add("NP",StringType,false)
      .add("open_date",DateType,false)
      .add("close_date",DateType,true)
      .add("col9",DoubleType,true)
      .add("col10",StringType,true)

    val df = spark
      .read
      .options(Map("delimiter"->";","header"->"false", "dateFormat"->"dd.MM.yyyy"))
      .schema(schema)
      .csv(path)
    
    df
  }
}
