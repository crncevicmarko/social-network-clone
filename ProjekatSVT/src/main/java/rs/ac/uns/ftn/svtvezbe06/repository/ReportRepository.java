package rs.ac.uns.ftn.svtvezbe06.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer>{
	public Report save(Report report);
	public Report findOneById(int id);
	public List<Report> findAll();
}
