from pyspark.sql import SparkSession

APP_NAME = "CAP-lab3"
SPARK_URL = "spark://192.168.49.2:30077"
spark = SparkSession.builder.appName(APP_NAME).master(
    SPARK_URL).config("spark.driver.host", "192.168.49.1").getOrCreate()
sc = spark.sparkContext

rdd = sc.parallelize([1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
rdd = rdd.map(lambda x: x*2)
rdd = rdd.reduce(lambda x, y: x+y)

print(rdd)
