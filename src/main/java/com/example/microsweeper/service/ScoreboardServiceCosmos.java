package com.example.microsweeper.service;

import com.example.microsweeper.model.Score;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.example.microsweeper.service.EnvironmentType.PRODUCTION;

@ApplicationScoped
@EnvironmentAlternative(PRODUCTION)
public class ScoreboardServiceCosmos implements ScoreboardService {

    private MongoClient mongoClient;

    //Comment this ad replace with below.

    @Inject
    @ConfigProperty(name="SCORESDB_uri")
    private String uri;
    
    //private String uri = "mongodb://microsweeper:3dQRF3DQPgHt0oDqKlWcH64GKHcS1HYsrmy1lOsBWOAV2FE9hYw8ONswHshSXgy0VA==@demo.documents.azure.com:443/?ssl=true&replicaSet=globaldb";

    private Logger LOG = Logger.getLogger(ScoreboardServiceCosmos.class.getName());

    @PostConstruct
    public void connect() {
        mongoClient = new MongoClient(new MongoClientURI(uri));
    }

    @Override
    public List<Score> getScoreboard() {
        List<Score> scores = new ArrayList<>();

        for (Document document : getScoresCollection().find()) {
            scores.add(Score.fromDocument(document));
        }
        LOG.info("Fetched scores from AzureDB: " + scores);
        return scores;

    }

    @Override
    @Transactional
    public void addScore(Score score) {
        createScoreItem(score);
        LOG.info("Stored score in AzureDB: " + score);
    }

    @Override
    public void clearScores() {
        getScoresCollection().drop();
        collectionCache = null;
        LOG.info("Cleared scores in AzureDB");
    }


    // Cache for the database object, so we don't have to query for it to
    // retrieve self links.
    private static MongoDatabase databaseCache;

    // Cache for the collection object, so we don't have to query for it to
    // retrieve self links.
    private static MongoCollection<Document> collectionCache;

    // The name of our database.
    private static final String DATABASE_ID = "ScoresDB";

    // The name of our collection.
    private static final String COLLECTION_ID = "ScoresCollection";

    private void createScoreItem(Score score) {
        Document scoreItemDocument = new Document(score.toMap());
        // Persist the document using the DocumentClient.
        getScoresCollection().insertOne(scoreItemDocument);

    }

    private MongoDatabase getScoreDatabase() {
        if (databaseCache != null) {
            return databaseCache;
        } else {
            databaseCache = mongoClient.getDatabase(DATABASE_ID);
            return databaseCache;
        }

    }

    private MongoCollection<Document> getScoresCollection() {
        if (collectionCache != null) {
            return collectionCache;
        } else {
            collectionCache = getScoreDatabase().getCollection(COLLECTION_ID);
            return collectionCache;
        }
    }
}