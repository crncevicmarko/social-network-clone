package rs.ac.uns.ftn.svtvezbe06.service;

import java.util.List;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Report;

public interface ReportService {
	
	public Report save(Report report);
	public Report findOneById(int id);
	public List<Report> findAll();
}
