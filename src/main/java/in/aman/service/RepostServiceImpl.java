package in.aman.service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.aman.entity.CitizenPlan;
import in.aman.repo.CitizenPlanRepository;
import in.aman.request.SearchRequest;
import in.aman.util.EmailUtils;
import in.aman.util.ExcelGenerator;
import in.aman.util.PdfGenerator;

@Service
public class RepostServiceImpl implements ReportService {

	@Autowired
	private CitizenPlanRepository planRepo;

	@Autowired
	private ExcelGenerator excelGenerator;
	
	@Autowired
	private PdfGenerator pdfGenerator;
	
	@Autowired
	private EmailUtils emailUtils;

	
	@Override
	public List<String> getPlanNames() {
		return planRepo.getPlanName();
	}

	@Override
	public List<String> getPlanStatus() {
		return planRepo.getPlanStatus();
	}

	@Override
	public List<CitizenPlan> search(SearchRequest request) {

		CitizenPlan entity = new CitizenPlan();

		if (null != request.getPlanName() && !"".equals(request.getPlanName())) {			
			entity.setPlanName(request.getPlanName());
		}
		
		if (null != request.getPlanStatus() && !"".equals(request.getPlanStatus())) {			
			entity.setPlanStatus(request.getPlanStatus());
		}
		
		if (null != request.getGender() && !"".equals(request.getGender())) {			
			entity.setGender(request.getGender());
		}
		
		if (null != request.getStartDate() && !"".equals(request.getStartDate())) {
			
			String startDate = request.getStartDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");	
			LocalDate localDate = LocalDate.parse(startDate, formatter);			
			entity.setPlanStartDate(localDate);
		}
		return planRepo.findAll(Example.of(entity));
	}

	@Override
	public boolean exportExcel(HttpServletResponse response) throws Exception {
		
		File f = new File("Plans.xls");
		List<CitizenPlan> plans = planRepo.findAll();	
		excelGenerator.generator(response, plans, f);
		
		String subject = "Test Mail Subject";
		String body = "<h1> Test Mail Body </h1>";
		String to = "mr.aman.man@gmail.com";

		emailUtils.sendEmail(subject, body, to, f);
		
		f.delete();
		
		return true;
	} 

	@Override
	public boolean exportPdf(HttpServletResponse response) throws Exception {

		File f = new File("Plans.pdf");
		List<CitizenPlan> plans = planRepo.findAll();
		pdfGenerator.generator(response, plans, f);

		String subject = "Test Mail Subject";
		String body = "<h1> Test Mail Body </h1>";
		String to = "mr.a.iamthelordvoldemort@gmail.com";

		emailUtils.sendEmail(subject, body, to, f);
		
		f.delete();

		return true;
	}

}
