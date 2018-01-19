package com.bzw.common.sequence;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 *
 * @author yanbin
 * @date 2017/7/8
 */
@Service
public class SequenceService implements ISequence {

    private MongoTemplate mongoTemplate;
    private static final String TABLE = "table";
    private static final String VALUE = "nextValue";
    private static final Long INIT_ID = 10000L;

    @Autowired
    public SequenceService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Long newKey(SeqType seqType) {
        String key = seqType.getKey();
        initKey(key);
        Query query = Query.query(Criteria.where(TABLE).is(key));
        Update update = new Update().inc(VALUE, 1);
        Sequence sequence = mongoTemplate.findAndModify(query, update, Sequence.class);
        return sequence.getNextValue();
    }

    private void initKey(String table) {
        Query query = Query.query(Criteria.where(TABLE).is(table));
        List<Sequence> sequenceList = mongoTemplate.find(query,Sequence.class);
        if (CollectionUtils.isEmpty(sequenceList)){
            mongoTemplate.insert(new Sequence(table, INIT_ID));
        }
    }

    @Override
    public List<Long> newKeys(SeqType seqType, int size) {
        String table = seqType.getKey();
        initKey(table);
        Query query = Query.query(Criteria.where(TABLE).is(table));
        Update update = new Update().inc(VALUE, size);
        Sequence sequence = mongoTemplate.findAndModify(query, update, Sequence.class);
        List<Long> result = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            result.add(sequence.getNextValue() + i);
        }
        return result;
    }
}
