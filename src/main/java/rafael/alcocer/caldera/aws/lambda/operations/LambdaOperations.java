/**
 * Copyright [2021] [RAFAEL ALCOCER CALDERA]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rafael.alcocer.caldera.aws.lambda.operations;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.DeleteFunctionRequest;
import com.amazonaws.services.lambda.model.FunctionConfiguration;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.ListFunctionsResult;
import com.amazonaws.services.lambda.model.ServiceException;

public class LambdaOperations {

    public static final String AWS_REGION = "us-east-1";
    public static final String ENDPOINT = "http://localhost:4566";
    public static final String FUNCTION_NAME = "awslambdalocalstack";
    
    public static void main(String[] args) {
        listLambdaFunctions();
        //invokeLambdaFunction(FUNCTION_NAME);
        //deleteLambdaFunction(FUNCTION_NAME);
    }
    
    public static void listLambdaFunctions() {
        ListFunctionsResult functionResult = null;

        try {
            AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(ENDPOINT, AWS_REGION);

            AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
                    .withEndpointConfiguration(endpointConfiguration)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            functionResult = awsLambda.listFunctions();

            List<FunctionConfiguration> functions = functionResult.getFunctions();
            
            functions.forEach(functionConfiguration -> System.out.println("##### Function name: " + functionConfiguration.getFunctionName()));
        } catch (ServiceException e) {
            System.out.println(e);
        }
    }
    
    public static void invokeLambdaFunction(String functionName) {
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName(functionName);
        
        InvokeResult invokeResult = null;

        try {
            AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(ENDPOINT, AWS_REGION);
            
            AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
                    .withEndpointConfiguration(endpointConfiguration)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            invokeResult = awsLambda.invoke(invokeRequest);

            String ans = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);

            // write out the return value
            System.out.println("##### ans: " + ans);
            
            System.out.println("##### invokeResult.getStatusCode(): " + invokeResult.getStatusCode());
            System.out.println("##### invokeResult.getPayload(): " + invokeResult.getPayload());
            System.out.println("##### invokeResult.getExecutedVersion(): " + invokeResult.getExecutedVersion());
            System.out.println("##### invokeResult.getLogResult(): " + invokeResult.getLogResult());
            System.out.println("##### invokeResult.getFunctionError(): " + invokeResult.getFunctionError());
            System.out.println("##### invokeResult.getSdkResponseMetadata(): " + invokeResult.getSdkResponseMetadata());
            System.out.println("##### invokeResult.getSdkResponseMetadata().getRequestId(): " + invokeResult.getSdkResponseMetadata().getRequestId());
            System.out.println("##### invokeResult.getSdkHttpMetadata(): " + invokeResult.getSdkHttpMetadata());
        } catch (ServiceException e) {
            System.out.println(e);
        }
    }
    
    public static void deleteLambdaFunction(String functionName) {
        try {
            AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(ENDPOINT, AWS_REGION);
            
            AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
                    .withEndpointConfiguration(endpointConfiguration)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            DeleteFunctionRequest delFunc = new DeleteFunctionRequest();
            delFunc.withFunctionName(functionName);

            // Delete the function
            awsLambda.deleteFunction(delFunc);
            System.out.println("The function was deleted");
        } catch (ServiceException e) {
            System.out.println(e);
        }
    }
}
