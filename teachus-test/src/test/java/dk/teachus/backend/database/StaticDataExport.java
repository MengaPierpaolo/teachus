/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.backend.database;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlWriter;

public class StaticDataExport {
	public static void main(String[] args) throws Exception {
		// database connection
		Class.forName("com.mysql.jdbc.Driver");
		Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost/teachus", "root", "");
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

		// Create dataset
		ITableFilter filter = new DatabaseSequenceFilter(connection);
		ReplacementDataSet dataset = new ReplacementDataSet(new FilteredDataSet(filter, connection.createDataSet()));
		dataset.addReplacementSubstring("\n", "\\n");
		
        // write DTD file
        FlatDtdDataSet.write(dataset, new FileOutputStream("src/test/resources/full.dtd"));
		
		// full database export
		FlatXmlWriter datasetWriter = new FlatXmlWriter( new FileOutputStream("src/test/resources/full.xml")); 
	    datasetWriter.setDocType("src/test/resources/full.dtd"); 
	    datasetWriter.write(dataset);
	}
}
