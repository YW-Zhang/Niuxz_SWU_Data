package com.nxz.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.nxz.model.Company;

public class Company_Persistant {
	public Company_Persistant() throws IOException {
		fileWriter = new FileWriter(file_name, true);
	}
	
	public void insert_to_file( Company company ) throws IOException {
		fileWriter.write(company.getCompanyName() + "\n");
		fileWriter.write(company.getCompanyDate() + "\n");
		fileWriter.write(company.getCompanyAddress() + "\n");
		fileWriter.write(company.getCompanyContent() + "\n");
		/*fileWriter.write("positions:");
		for(int i = 0; i < company.getPositions().size(); i++) {
			fileWriter.write(company.getPositions().get(i));
		}*/
		fileWriter.write("\n\n\n\n\n");
		fileWriter.flush();
		fileWriter.close();
	}
	private FileWriter fileWriter;
	private static final String file_name = "result.txt";
}
