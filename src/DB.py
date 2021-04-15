import boto3
import csv

s3 = boto3.resource('s3', aws_access_key_id = '', aws_secret_access_key = '')

try:
	s3.create_bucket(Bucket = '', CreateBucketConfiguration = {'LocationConstraint': 'us-east-2'})
except:
	print("bucket may already exist")

#s3.Object('myawsbucket88472', 'test.png').put(Body=open('C:\\Users\\jarod_000\\Desktop\\CS 1660\\AWS HW\\test.png', 'rb'))

dyndb = boto3.resource('dynamodb', region_name = 'us-east-2', aws_access_key_id = '', aws_secret_access_key = '')

table = ""

try:
	table = dyndb.create_table(
		TableName = 'DataTable',
		KeySchema = [
			{ 'AttributeName': 'DocID', 'KeyType': 'HASH'},
			{ 'AttributeName': 'Folder', 'KeyType': 'RANGE'},
			{ 'AttributeName': 'DocName', 'KeyType': 'RANGE'},
			{ 'AttributeName': 'Term', 'KeyType': 'RANGE'},
			{ 'AttributeName': 'Frequency', 'KeyType': 'RANGE'}
		],
		AttributeDefinitions = [
			{ 'AttributeName': 'DocID', 'AttributeType': 'I'},
			{ 'AttributeName': 'Folder', 'AttributeType': 'S'},
			{ 'AttributeName': 'DocName', 'AttributeType': 'S'},
			{ 'AttributeName': 'Term', 'AttributeType': 'S'},
			{ 'AttributeName': 'Frequency', 'AttributeType': 'I'}
		],
		ProvisionedThroughput={
			'ReadCapacityUnits': 5,
			'WriteCapacityUnits': 5
		}
	)
except:
	print("exception in creating table")
	table = dyndb.Table("DataTable")

table.meta.client.get_waiter('table_exists').wait(TableName = 'DataTable')

response = table.get_item(
	Key={
		'PartitionKey': 'experiment2',
		'RowKey': 'data2'
	}
)

item = response['Item']
print(item)
