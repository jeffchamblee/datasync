package com.socrata.datasync;

import com.socrata.api.Soda2Producer;
import com.socrata.api.SodaDdl;
import com.socrata.exceptions.LongRunningQueryException;
import com.socrata.exceptions.SodaError;
import com.socrata.model.UpsertResult;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Author: Adrian Laurenzi
 * Date: 10/11/13
 */
public class IntegrationUtilityTest extends TestBase {

    @Test
    public void testReplaceNew() throws LongRunningQueryException, SodaError, IOException, InterruptedException {
        final Soda2Producer producer = createProducer();

        File twoRowsFile = new File("src/test/resources/datasync_unit_test_two_rows.csv");
        UpsertResult result = IntegrationUtility.replaceNew(producer, UNITTEST_DATASET_ID, twoRowsFile);

        TestCase.assertEquals(0, result.errorCount());
        TestCase.assertEquals(2, getTotalRowsUnitTestDataset());
    }

    @Test
    public void testUpsertZeroRows() throws IOException, SodaError, InterruptedException, LongRunningQueryException {
        final Soda2Producer producer = createProducer();
        final SodaDdl ddl = createSodaDdl();

        int numRowsBegin = getTotalRowsUnitTestDataset();

        File zeroRowsFile = new File("src/test/resources/datasync_unit_test_zero_rows.csv");
        UpsertResult result = IntegrationUtility.upsert(producer, ddl, UNITTEST_DATASET_ID, null, zeroRowsFile);

        int numRowsAfter = getTotalRowsUnitTestDataset();

        TestCase.assertEquals(0, result.errorCount());
        TestCase.assertEquals(numRowsBegin, numRowsAfter);
    }

    @Test
    public void testUpsertNoDeletes() throws IOException, SodaError, InterruptedException, LongRunningQueryException {
        final Soda2Producer producer = createProducer();
        final SodaDdl ddl = createSodaDdl();

        // Ensures dataset is in known state (2 rows)
        File twoRowsFile = new File("src/test/resources/datasync_unit_test_two_rows.csv");
        IntegrationUtility.replaceNew(producer, UNITTEST_DATASET_ID, twoRowsFile);

        File threeRowsFile = new File("src/test/resources/datasync_unit_test_three_rows.csv");
        UpsertResult result = IntegrationUtility.upsert(producer, ddl, UNITTEST_DATASET_ID, null, threeRowsFile);

        TestCase.assertEquals(0, result.errorCount());
        TestCase.assertEquals(2, result.getRowsUpdated());
        TestCase.assertEquals(1, result.getRowsCreated());
        TestCase.assertEquals(3, getTotalRowsUnitTestDataset());
    }

    @Test
    public void testUpsertWithDeletes() {

    }

    @Test
    public void testUpsertInChunksNoDeletes() {

    }

    @Test
    public void testUpsertInChunksWithDeletes() {

    }

    @Test
    public void testUpsertWithBadData() {

    }

    @Test
    public void testReplaceNewWithBadData() {

    }

    @Test
    public void testAddLogEntry() {

    }
}