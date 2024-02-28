package com.innoseti.innosetitestprojec.exceptionResolver;

import com.innoseti.innosetitestprojec.exception.ExistsInDataBaseException;
import com.innoseti.innosetitestprojec.exception.NotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;

import org.springframework.stereotype.Component;

@Component
public class ExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof NotFoundException) {
            return getGraphQLError(ex, env, ErrorType.NOT_FOUND);
        } else if (ex instanceof ExistsInDataBaseException) {
            return getGraphQLError(ex, env, ErrorType.SAVE_EXCEPTION);
        } else {
            return null;
        }
    }

    private static GraphQLError getGraphQLError(Throwable ex, DataFetchingEnvironment env, ErrorType errorType) {
        return GraphqlErrorBuilder.newError()
                .errorType(errorType)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }
}
