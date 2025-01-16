package com.spring.example.util;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.spring.example.model.SearchRequest;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Supplier;

@UtilityClass
public class ElasticSearchUtil {

    public static Supplier<Query> buildQueryForFieldAndValue(String fieldName, String searchValue) {
        return () -> Query.of(q -> q.match(buildMatchQueryForFieldAndValue(fieldName, searchValue)));
    }

    /**
     * **matchQuery:** `matchQuery` kelime bazlı bir arama yapar.
     * Bu sorgu, belirli bir alanın belirli bir metin değere karşılık geldiği durumlarda kullanılır.
     * Örneğin, belirli bir isme sahip belgeleri aramak isterseniz, bir `matchQuery` kullanırız. Kısmi eşleşmeler ve kelime bazlı aramalar için uygundur.
     * @param fieldName
     * @param searchValue
     * @return
     */
    private static MatchQuery buildMatchQueryForFieldAndValue(String fieldName, String searchValue) {
        return new MatchQuery.Builder()
                .field(fieldName)
                .query(searchValue)
                .build();
    }

    /**
     * **boolQuery:** `boolQuery` daha karmaşık ve çok yönlü bir sorgudur. İçerisine `must`, `should`, `must_not` gibi
     * birçok farklı türde alt sorgu ekleyerek farklı bool işlemlerini gerçekleştirebilirsiniz.
     * Bu nedenle, belgelerin birden fazla koşulu karşılaması gerektiğinde kullanılır.
     * Hem match sorguları hem de diğer bool sorguları içerebilir.
     */
    public static Supplier<Query> createBoolQuery(List<SearchRequest> dto) {
        return () -> Query.of(q -> q.bool(boolQuery(dto.get(0).getName(), dto.get(0).getValue(),
                dto.get(1).getName(), dto.get(1).getValue())));
    }

    private static BoolQuery boolQuery(String key1, String value1, String key2, String value2) {
        return new BoolQuery.Builder()
                .filter(termQuery(key1, value1))
                .must(matchQuery(key2, value2))
                .build();
    }

    /**
     * `termQuery`, Elasticsearch'te bir başka çok kullanılan sorgu türüdür. Bu sorgu türü, belirtilen tam terim veya terimlerin
     * belirtilen alanda olduğu belgeleri bulmak için kullanılır.
     * Bu, `matchQuery`'nin aksine, tam bir terim eşleşmesi arar ve analizörleri (lowercase dönüşümü, stopwords kaldırma vb.) atlar.
     * Bu özellik, belirli bir değeri tam olarak taşıyan belgelerin kesin eşleşmelerini bulmak için kullanılır.
     * Genellikle tam terim, kesin değer veya keyword tipindeki alanlarda arama yapmak için kullanılır.
     * @param key
     * @param value
     * @return
     */
    private static Query termQuery(String key, String value) {
        return Query.of(q -> q.term(new TermQuery.Builder().field(key).value(value).build()));
    }

    private static Query matchQuery(String key, String value) {
        return Query.of(q -> q.match(new MatchQuery.Builder().field(key).query(value).build()));
    }


}
