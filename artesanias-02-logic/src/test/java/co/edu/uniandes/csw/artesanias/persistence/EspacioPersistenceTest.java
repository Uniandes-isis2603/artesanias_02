/*
 * The MIT License
 *
 * Copyright 2017 Miller.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.edu.uniandes.csw.artesanias.persistence;

import co.edu.uniandes.csw.artesanias.entities.CiudadEntity;
import co.edu.uniandes.csw.artesanias.entities.EspacioEntity;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author Miller
 */
@RunWith(Arquillian.class)
public class EspacioPersistenceTest extends PersistenceTest<EspacioEntity> {

    @Inject
    private EspacioPersistence persistence;
    
    private CiudadEntity c1;
    private CiudadEntity c2;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(EspacioEntity.class.getPackage())
                .addPackage(EspacioPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    protected void clearData() {
        em.createQuery("DELETE FROM EspacioEntity ").executeUpdate();
    }

    protected void insertData() {
        PodamFactory factory = new PodamFactoryImpl();

        c1 = c1 == null ? factory.manufacturePojo(CiudadEntity.class) : c1;
        c2 = c2 == null ? factory.manufacturePojo(CiudadEntity.class) : c2;

        List<EspacioEntity> espacios1 = new LinkedList<EspacioEntity>();
        List<EspacioEntity> espacios2 = new LinkedList<EspacioEntity>();

        for (int i = 0; i < 10; i++) {
            EspacioEntity entity = factory.manufacturePojo(EspacioEntity.class);
            entity.setCiudad(i % 2 == 0 ? c1 : c2);

            em.persist(entity);
            data.add(entity);

            if (i % 2 == 0) {
                espacios1.add(entity);
            } else {
                espacios2.add(entity);
            }
        }
        c1.setEspacios(espacios1);
        c2.setEspacios(espacios2);
    }

    @Test
    public void create() throws Exception {
        PodamFactory factory = new PodamFactoryImpl();

        EspacioEntity newEntity = factory.manufacturePojo(EspacioEntity.class);
        c1 = factory.manufacturePojo(CiudadEntity.class);
        c2 = factory.manufacturePojo(CiudadEntity.class);

        EspacioEntity result = persistence.create(newEntity);
        Assert.assertNotNull(result);

        EspacioEntity entity = em.find(EspacioEntity.class, result.getId());
        Assert.assertNotNull(entity);
    }

    @Test
    public void find() throws Exception {
        EspacioEntity entity = c1.getEspacios().get(0);
        EspacioEntity newEntity = persistence.find(entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getId(), newEntity.getId());
        entity = c2.getEspacios().get(0);
        newEntity = persistence.find(entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getId(), newEntity.getId());
    }

    @Test
    public void findAll() {
        List<EspacioEntity> finded = persistence.findAll();
        Assert.assertEquals(data.size(), finded.size());
        for (EspacioEntity espacioEntity : finded) {
            boolean found = false;
            for (EspacioEntity entity : data) {
                if (espacioEntity.getId().equals(entity.getId())) {
                    found = true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void update() {
        EspacioEntity entity = data.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        EspacioEntity upEntity = factory.manufacturePojo(EspacioEntity.class);
        upEntity.setId(entity.getId());

        persistence.update(upEntity);

        EspacioEntity resp = em.find(EspacioEntity.class, entity.getId());
        Assert.assertEquals(upEntity.getNombre(), resp.getNombre());
    }

    @Test
    public void delete() {
        EspacioEntity entity = data.get(0);
        persistence.delete(entity.getId());
        EspacioEntity deleted = em.find(EspacioEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

}
