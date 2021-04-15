import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.AmazonServiceException;
import java.io.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import java.util.Scanner;


public class AmazonStorage
{
    private static AmazonS3 s3;
    private String bucket_name = "";
    private String region = "";

    private BasicAWSCredentials awsCreds = new BasicAWSCredentials("", "");

    public AmazonStorage() throws Exception
    {
        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(region)
                .build();

        if (s3.doesBucketExistV2(bucket_name)) {
            System.out.format("\nCannot create the bucket. \n" +
                    "A bucket named '%s' already exists.", bucket_name);
            return;
        } else {
            try {
                System.out.format("\nCreating a new bucket named '%s'...\n\n", bucket_name);
                s3.createBucket(new CreateBucketRequest(bucket_name, region));
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
    }

    public void putFile(File file) throws Exception{
        final String USAGE = "\n" +
                "To run this example, supply the name of an S3 bucket and a file to\n" +
                "upload to it.\n" +
                "\n" +
                "Ex: PutObject <bucketname> <filename>\n";

        String key_name = file.getPath();

        System.out.format("Uploading file to S3 bucket %s...\n", bucket_name);
        try {
            s3.putObject(bucket_name, key_name, file);
            executeWordCount(key_name, file);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }



        System.out.println("Done!");
    }

    public void executeWordCount(String key_name, File file) throws Exception
    {
        int count = 0;
        String folder = "";
        String fileName = "";
        String output_key = "";
        // run wordcount on each file

        try {
            String awsCliCommand = "aws s3 ls";
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \""+awsCliCommand+"\"");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("DataTable");

        Object outputObject = s3.getObject(bucket_name, output_key);

        File outputFile = new File("test.txt");

        Scanner reader = new Scanner(outputFile);

        while(reader.hasNextLine())
        {
            String line = reader.next();
            int wordCount = reader.nextInt();

            Item item = new Item().withPrimaryKey("DocID", count++).withString("Folder", folder)
                    .withString("DocName", fileName)
                    .withString("Term", line)
                    .withNumber("Frequency", wordCount);
            table.putItem(item);
        }

    }

    public Object[][] fetchFiles(String term)
    {
        // Search by Term, combine all counts
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("DataTable");

        return null;
    }

    public Object[][] fetchTerms(int num)
    {
        // Group by term, combine totals
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("DataTable");

        return null;
    }
}
