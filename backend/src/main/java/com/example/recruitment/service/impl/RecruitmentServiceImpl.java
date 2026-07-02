package com.example.recruitment.service.impl;

import com.example.recruitment.dto.ApplicationCreateRequest;
import com.example.recruitment.dto.StatisticsResponse;
import com.example.recruitment.model.Application;
import com.example.recruitment.model.Job;
import com.example.recruitment.model.Message;
import com.example.recruitment.service.RecruitmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class RecruitmentServiceImpl implements RecruitmentService {

    private final ConcurrentHashMap<Long, Job> jobs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Application> applications = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Message> messages = new ConcurrentHashMap<>();

    private final AtomicLong jobIdGenerator = new AtomicLong(1);
    private final AtomicLong applicationIdGenerator = new AtomicLong(1);
    private final AtomicLong messageIdGenerator = new AtomicLong(1);

    public RecruitmentServiceImpl() {
        initMockData();
    }

    private void initMockData() {
        LocalDateTime now = LocalDateTime.now();

        Job job1 = new Job();
        job1.setId(jobIdGenerator.get());
        job1.setTitle("Java后端开发工程师");
        job1.setCompany("字节跳动");
        job1.setCity("北京");
        job1.setDescription("负责公司核心业务系统的后端开发，参与系统架构设计和优化。");
        job1.setRequirements("1. 本科及以上学历，计算机相关专业\n2. 3年以上Java开发经验\n3. 熟悉Spring Boot、MySQL等技术栈");
        job1.setStatus("ACTIVE");
        job1.setCreatedAt(now.minusDays(3));
        job1.setUpdatedAt(now.minusDays(3));
        job1.setMinSalary(25);
        job1.setMaxSalary(45);
        job1.setPosition("后端开发");
        job1.setDepartment("技术部");
        job1.setExperienceMin(3);
        job1.setExperienceMax(5);
        job1.setEducation("本科");
        job1.setEmployerName("字节跳动HR");
        job1.setRequiredSkills(Arrays.asList("Java", "Spring Boot", "MySQL", "Redis", "微服务"));
        job1.setViewCount(128);
        job1.setApplyCount(2);
        job1.setResponsibilities("1. 负责后端系统的设计与开发\n2. 参与技术方案评审\n3. 优化系统性能");
        jobs.put(jobIdGenerator.getAndIncrement(), job1);

        Job job2 = new Job();
        job2.setId(jobIdGenerator.get());
        job2.setTitle("前端开发工程师");
        job2.setCompany("阿里巴巴");
        job2.setCity("杭州");
        job2.setDescription("负责电商平台前端页面开发，优化用户体验和页面性能。");
        job2.setRequirements("1. 本科及以上学历\n2. 2年以上前端开发经验\n3. 精通Vue/React、TypeScript");
        job2.setStatus("ACTIVE");
        job2.setCreatedAt(now.minusDays(2));
        job2.setUpdatedAt(now.minusDays(2));
        job2.setMinSalary(20);
        job2.setMaxSalary(40);
        job2.setPosition("前端开发");
        job2.setDepartment("技术部");
        job2.setExperienceMin(2);
        job2.setExperienceMax(4);
        job2.setEducation("本科");
        job2.setEmployerName("阿里巴巴HR");
        job2.setRequiredSkills(Arrays.asList("React", "TypeScript", "Redux", "Webpack"));
        job2.setViewCount(96);
        job2.setApplyCount(1);
        job2.setResponsibilities("1. 负责前端页面开发\n2. 优化页面性能\n3. 维护代码质量");
        jobs.put(jobIdGenerator.getAndIncrement(), job2);

        Job job3 = new Job();
        job3.setId(jobIdGenerator.get());
        job3.setTitle("产品经理");
        job3.setCompany("腾讯");
        job3.setCity("深圳");
        job3.setDescription("负责产品规划、需求分析和项目管理，推动产品迭代优化。");
        job3.setRequirements("1. 本科及以上学历\n2. 3年以上产品经理经验\n3. 有互联网产品经验优先");
        job3.setStatus("ACTIVE");
        job3.setCreatedAt(now.minusDays(1));
        job3.setUpdatedAt(now.minusDays(1));
        job3.setMinSalary(22);
        job3.setMaxSalary(42);
        job3.setPosition("产品设计");
        job3.setDepartment("产品部");
        job3.setExperienceMin(3);
        job3.setExperienceMax(5);
        job3.setEducation("本科");
        job3.setEmployerName("腾讯HR");
        job3.setRequiredSkills(Arrays.asList("产品设计", "用户研究", "数据分析"));
        job3.setViewCount(78);
        job3.setApplyCount(1);
        job3.setResponsibilities("1. 产品规划与设计\n2. 需求分析与管理\n3. 项目进度跟进");
        jobs.put(jobIdGenerator.getAndIncrement(), job3);

        Job job4 = new Job();
        job4.setId(jobIdGenerator.get());
        job4.setTitle("测试工程师");
        job4.setCompany("美团");
        job4.setCity("上海");
        job4.setDescription("负责产品质量保障，设计测试用例，执行测试任务。");
        job4.setRequirements("1. 本科及以上学历\n2. 2年以上软件测试经验\n3. 熟悉自动化测试工具");
        job4.setStatus("CLOSED");
        job4.setCreatedAt(now.minusDays(7));
        job4.setUpdatedAt(now.minusDays(1));
        job4.setMinSalary(15);
        job4.setMaxSalary(25);
        job4.setPosition("测试开发");
        job4.setDepartment("质量部");
        job4.setExperienceMin(2);
        job4.setExperienceMax(4);
        job4.setEducation("本科");
        job4.setEmployerName("美团HR");
        job4.setRequiredSkills(Arrays.asList("测试用例设计", "自动化测试", "缺陷管理"));
        job4.setViewCount(56);
        job4.setApplyCount(0);
        job4.setResponsibilities("1. 设计测试用例\n2. 执行测试任务\n3. 提交缺陷报告");
        jobs.put(jobIdGenerator.getAndIncrement(), job4);

        Application app1 = new Application();
        app1.setId(applicationIdGenerator.get());
        app1.setJobId(1L);
        app1.setJobTitle("Java后端开发工程师");
        app1.setApplicantName("张三");
        app1.setApplicantPhone("13800138001");
        app1.setApplicantEmail("zhangsan@example.com");
        app1.setResumeSummary("5年Java开发经验，曾在知名互联网公司任职，主导过多个大型项目。");
        app1.setWorkYears(5);
        app1.setSkills(Arrays.asList("Java", "Spring Boot", "MySQL", "Redis"));
        app1.setStatus("PENDING");
        app1.setCreatedAt(now.minusDays(2));
        app1.setUpdatedAt(now.minusDays(2));
        applications.put(applicationIdGenerator.getAndIncrement(), app1);

        Application app2 = new Application();
        app2.setId(applicationIdGenerator.get());
        app2.setJobId(1L);
        app2.setJobTitle("Java后端开发工程师");
        app2.setApplicantName("李四");
        app2.setApplicantPhone("13800138002");
        app2.setApplicantEmail("lisi@example.com");
        app2.setResumeSummary("3年Java开发经验，熟悉微服务架构，有良好的团队协作能力。");
        app2.setWorkYears(3);
        app2.setSkills(Arrays.asList("Java", "Spring Cloud", "Docker", "Kubernetes"));
        app2.setStatus("COMMUNICATING");
        app2.setCreatedAt(now.minusDays(1));
        app2.setUpdatedAt(now.minusHours(2));
        applications.put(applicationIdGenerator.getAndIncrement(), app2);

        Application app3 = new Application();
        app3.setId(applicationIdGenerator.get());
        app3.setJobId(2L);
        app3.setJobTitle("前端开发工程师");
        app3.setApplicantName("王五");
        app3.setApplicantPhone("13800138003");
        app3.setApplicantEmail("wangwu@example.com");
        app3.setResumeSummary("4年前端开发经验，精通React生态，有丰富的性能优化经验。");
        app3.setWorkYears(4);
        app3.setSkills(Arrays.asList("React", "TypeScript", "Redux", "Webpack"));
        app3.setStatus("PENDING");
        app3.setCreatedAt(now.minusHours(5));
        app3.setUpdatedAt(now.minusHours(5));
        applications.put(applicationIdGenerator.getAndIncrement(), app3);

        Application app4 = new Application();
        app4.setId(applicationIdGenerator.get());
        app4.setJobId(3L);
        app4.setJobTitle("产品经理");
        app4.setApplicantName("赵六");
        app4.setApplicantPhone("13800138004");
        app4.setApplicantEmail("zhaoliu@example.com");
        app4.setResumeSummary("2年产品经理经验，擅长用户研究和需求分析，有B端产品经验。");
        app4.setWorkYears(2);
        app4.setSkills(Arrays.asList("产品设计", "用户研究", "数据分析"));
        app4.setStatus("REJECTED");
        app4.setCreatedAt(now.minusDays(1));
        app4.setUpdatedAt(now.minusHours(3));
        applications.put(applicationIdGenerator.getAndIncrement(), app4);

        Message msg1 = new Message(
                messageIdGenerator.getAndIncrement(),
                2L,
                "EMPLOYER",
                "字节跳动HR",
                "您好，您的简历已通过初筛，请问明天下午3点方便进行面试吗？",
                now.minusHours(3)
        );
        messages.put(msg1.getId(), msg1);

        Message msg2 = new Message(
                messageIdGenerator.getAndIncrement(),
                2L,
                "APPLICANT",
                "李四",
                "您好，明天下午3点可以的，请问面试形式是线上还是线下？",
                now.minusHours(2)
        );
        messages.put(msg2.getId(), msg2);
    }

    @Override
    public List<Job> getAllJobs() {
        return new ArrayList<>(jobs.values());
    }

    @Override
    public List<Job> searchJobs(String title, String city, String status) {
        return jobs.values().stream()
                .filter(job -> {
                    if (title != null && !title.isEmpty()) {
                        if (!job.getTitle().contains(title) && !job.getPosition().contains(title)) {
                            return false;
                        }
                    }
                    if (city != null && !city.isEmpty() && !job.getCity().equals(city)) {
                        return false;
                    }
                    if (status != null && !status.isEmpty() && !job.getStatus().equals(status)) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Job getJobById(Long id) {
        return jobs.get(id);
    }

    @Override
    public Job createJob(String title, String company, String city, String description, String requirements) {
        return null;
    }

    public Job createJob(com.example.recruitment.dto.JobCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Job job = new Job();
        job.setId(jobIdGenerator.getAndIncrement());
        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setCity(request.getCity());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setStatus("ACTIVE");
        job.setCreatedAt(now);
        job.setUpdatedAt(now);
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());
        job.setPosition(request.getPosition());
        job.setDepartment(request.getDepartment());
        job.setExperienceMin(request.getExperienceMin());
        job.setExperienceMax(request.getExperienceMax());
        job.setEducation(request.getEducation());
        job.setEmployerName(request.getEmployerName());
        job.setRequiredSkills(request.getRequiredSkills());
        job.setViewCount(0);
        job.setApplyCount(0);
        job.setResponsibilities(request.getResponsibilities());
        jobs.put(job.getId(), job);
        return job;
    }

    @Override
    public void deleteJob(Long id) {
        jobs.remove(id);
    }

    public void updateJobStatus(Long id, String status) {
        Job job = jobs.get(id);
        if (job != null) {
            job.setStatus(status);
            job.setUpdatedAt(LocalDateTime.now());
            jobs.put(id, job);
        }
    }

    public List<String> getCities() {
        return jobs.values().stream()
                .map(Job::getCity)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getPositions() {
        return jobs.values().stream()
                .map(Job::getPosition)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<Application> getAllApplications() {
        return new ArrayList<>(applications.values());
    }

    @Override
    public List<Application> getApplicationsByJobId(Long jobId) {
        return applications.values().stream()
                .filter(app -> app.getJobId().equals(jobId))
                .collect(Collectors.toList());
    }

    public List<Application> getApplicationsByApplicant(String applicantName) {
        return applications.values().stream()
                .filter(app -> app.getApplicantName().equals(applicantName))
                .collect(Collectors.toList());
    }

    @Override
    public Application getApplicationById(Long id) {
        return applications.get(id);
    }

    @Override
    public Application createApplication(ApplicationCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Job job = jobs.get(request.getJobId());
        Application application = new Application();
        application.setId(applicationIdGenerator.getAndIncrement());
        application.setJobId(request.getJobId());
        application.setJobTitle(job != null ? job.getTitle() : "未知职位");
        application.setApplicantName(request.getApplicantName());
        application.setApplicantPhone(request.getApplicantPhone());
        application.setApplicantEmail(request.getApplicantEmail());
        application.setResumeSummary(request.getResumeSummary());
        application.setWorkYears(request.getWorkYears());
        application.setSkills(request.getSkills());
        application.setStatus("PENDING");
        application.setCreatedAt(now);
        application.setUpdatedAt(now);
        applications.put(application.getId(), application);

        if (job != null) {
            job.setApplyCount(job.getApplyCount() + 1);
            jobs.put(job.getId(), job);
        }
        return application;
    }

    @Override
    public Application updateApplicationStatus(Long id, String status) {
        Application application = applications.get(id);
        if (application != null) {
            application.setStatus(status);
            application.setUpdatedAt(LocalDateTime.now());
            applications.put(id, application);
        }
        return application;
    }

    @Override
    public List<Message> getMessagesByApplicationId(Long applicationId) {
        return messages.values().stream()
                .filter(msg -> msg.getApplicationId().equals(applicationId))
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public Message sendMessage(Long applicationId, String senderRole, String content) {
        return null;
    }

    public Message sendMessage(Long applicationId, String senderRole, String senderName, String content) {
        LocalDateTime now = LocalDateTime.now();
        Message message = new Message(
                messageIdGenerator.getAndIncrement(),
                applicationId,
                senderRole,
                senderName,
                content,
                now
        );
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public StatisticsResponse getStatistics() {
        LocalDate today = LocalDate.now();

        long activeJobs = jobs.values().stream()
                .filter(job -> "ACTIVE".equals(job.getStatus()))
                .count();

        long todayApplications = applications.values().stream()
                .filter(app -> app.getCreatedAt().toLocalDate().equals(today))
                .count();

        long communicatingCount = applications.values().stream()
                .filter(app -> "COMMUNICATING".equals(app.getStatus()))
                .count();

        long pendingApplications = applications.values().stream()
                .filter(app -> "PENDING".equals(app.getStatus()))
                .count();

        return new StatisticsResponse(activeJobs, todayApplications, communicatingCount, pendingApplications);
    }
}