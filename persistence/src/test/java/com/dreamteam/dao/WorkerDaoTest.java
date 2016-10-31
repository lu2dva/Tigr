package com.dreamteam.dao;

import com.dreamteam.TigrAppContext;
import com.dreamteam.entity.Worker;
import java.util.List;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for Worker DAO class.
 * @author Jiri Oliva
 */
@ContextConfiguration(classes = TigrAppContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public class WorkerDaoTest extends AbstractTestNGSpringContextTests{
//    @PersistenceContext
//    private EntityManager entityManager;
    
    @Autowired
    private WorkerDao workerDao;
    
    private Worker worker1;
    private Worker worker2;
    private Worker worker3;
    
    @BeforeClass
    public void createTestData() {
        worker1 = new Worker();
        worker2 = new Worker();
        worker3 = new Worker();
        
        worker1.setAdministrator(true);
        worker1.setEmail("worker1@dreamteam.com");
        worker1.setPasswordHash("unbreakable");
        
        workerDao.create(worker1);
        workerDao.create(worker2);
        workerDao.create(worker3);
    }
    
    @AfterClass
    public void clearTestData() {
        workerDao.delete(worker1);
        workerDao.delete(worker2);
        workerDao.delete(worker3);
    }
    
    @Test
    public void testGetAll() {
        List<Worker> result = workerDao.all();
        Assert.assertEquals(result.size(), 3);
    }
    
    @Test
    public void testFindById() {
        Worker result = workerDao.findById(worker1.getId());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getEmail(), worker1.getEmail());
        
        result = workerDao.findById(worker1.getId() + worker2.getId() + worker3.getId());
        Assert.assertNull(result);
        
        result = workerDao.findById(null);
        Assert.assertNull(result);
    }
    
    @Test
    public void testFindByEmail() {
        Worker result = workerDao.findWorkerByEmail(worker1.getEmail());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getEmail(), worker1.getEmail());
        
        result = workerDao.findWorkerByEmail("invalidEmail");
        Assert.assertNull(result);
        
        result = workerDao.findWorkerByEmail(null);
        Assert.assertNull(result);
    }
    
    @Test
    public void testUpdate() {
        Worker result = workerDao.findById(worker1.getId());
        Assert.assertEquals(result.getEmail(), "worker1@dreamteam.com");
        worker1.setEmail("updated@dreamteam.com");
        
        workerDao.update(worker1);
        result = workerDao.findById(worker1.getId());
        Assert.assertEquals(result.getEmail(), "updated@dreamteam.com");
        Assert.assertNotEquals(result.getEmail(), "worker1@dreamteam.com");
    }
    
    @Test
    public void testDelete() {
        Worker result = workerDao.findById(worker1.getId());
        Assert.assertNotNull(result);
        workerDao.delete(worker1);
        result = workerDao.findById(worker1.getId());
        Assert.assertNull(result);        
    }
    
}
