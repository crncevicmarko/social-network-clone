package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Report;
import rs.ac.uns.ftn.svtvezbe06.repository.ReportRepository;
import rs.ac.uns.ftn.svtvezbe06.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService{

	@Autowired
	private ReportRepository reportRepository;
	
	@Override
	public Report save(Report report) {
		return reportRepository.save(report);
	}

	@Override
	public List<Report> findAll() {
		return reportRepository.findAll();
	}

	@Override
	public Report findOneById(int id) {
		return reportRepository.findOneById(id);
	}
	

}
