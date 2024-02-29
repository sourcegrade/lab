import {
    ApolloClient,
    InMemoryCache,
    NormalizedCacheObject,
    HttpLink,
    ApolloProvider,
} from "@apollo/client";
// import { useRouter } from "next/router";
import withApollo from "next-with-apollo";

export const client = new ApolloClient({
    ssrMode: typeof window === "undefined",
    link: new HttpLink({
        uri: "http://localhost:8080/graphql",
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include",
        // fetchOptions: {
        //     mode: "no-cors",
        // },
    }),
    credentials: "include",
    cache: new InMemoryCache(),
});

export default withApollo(
    ({ initialState, headers }) => {
        return new ApolloClient({
            ssrMode: typeof window === "undefined",
            link: new HttpLink({
                uri: "http://localhost:8080/graphql",
            }),
            credentials: "include",
            headers: {
                ...(headers as Record<string, string>),
            },
            cache: new InMemoryCache().restore(initialState || {}),
        });
    },
    {
        render: ({ Page, props }) => {
            // const router = useRouter();
            return (
                <ApolloProvider client={props.apollo}>
                    <Page {...props}/>
                    {/* <Page {...props} router={router} /> */}
                </ApolloProvider>
            );
        },
    },
);
