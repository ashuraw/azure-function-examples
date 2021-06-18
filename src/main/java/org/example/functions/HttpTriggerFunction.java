package org.example.functions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;
/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerFunction {

    @FunctionName("fraudScore")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS)
                    HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        // Item list
        context.getLogger().info("Request body is: " + request.getBody().orElse(""));
        int num= (int) (Math.random()*100);
        // Check request body
        if (!request.getBody().isPresent()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Document not found.")
                    .build();
        }
        else {
            // return JSON from to the client
            // Generate document
            String ans;
            if(num<50)
                ans="Reject";
            else if(num<80)
                ans="Refer";
            else
                ans="Accept";
            final String body = request.getBody().get();
            final String jsonDocument = "{\"score\":\""+num+"\", " +
                    "\"decision\": \"" + ans + "\"}";
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(jsonDocument)
                    .build();
        }
    }
}
