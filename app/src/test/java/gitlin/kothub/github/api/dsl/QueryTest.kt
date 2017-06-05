package gitlin.kothub.github.api.dsl

import org.junit.Assert.*
import org.junit.Test
//
//
//class QueryTest {
//    @Test
//    fun testQuery() {
//        val q = query {
//            repository(value("fknop"), value("angular-pipes")) {
//                issues(first = value(10)) {
//                    edges {
//                        node {
//                            author {
//                                login
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        val pretty =
//"""
//query {
//  repository(owner: "fknop", name: "angular-pipes") {
//    issues(first: 10) {
//      edges {
//        node {
//          author {
//            login
//          }
//        }
//      }
//    }
//  }
//}"""
//
//        val raw = "query{repository(owner: \"fknop\", name: \"angular-pipes\"){issues(first: 10){edges{node{author{login }}}}}}"
//
//        //assertEquals("\n" + q.prettyPrint(), pretty)
//        //assertEquals(q.toString(), raw)
//    }
//}