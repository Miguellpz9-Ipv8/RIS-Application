package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "file_uploads")
public class FileUpload {
    @Id
    @Column(name = "file_upload_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "order_id")
    private Long order;
    @Column(name = "upload_path")
    private String filepath;
    @Column(name = "file_name")
    private String filename;
    @Column(name = "file_type")
    private String filetype;
    @Column(name = "is_active")
    private int active;


    //  GETTERS

    public Long getId(){
        return this.id;
    }

    public Long getOrder(){
        return this.order;
    }

    public String getFilepath(){
        return this.filepath;
    }

    public String getFilename(){
        return this.filename;
    }

    public String getFiletype(){
        return this.filetype;
    }

    public int getActive(){
        return this.active;
    }

    
    //  SETTERS

    public void setId(Long id){
        this.id = id;
    }
    
    public void setOrder(Long order){
        this.order = order;
    }

    public void setFilepath(String filepath){
        this.filepath = filepath;
    }

    public void setFilename(String filename){
        this.filename = filename;
    }

    public void setFiletype(String filetype){
        this.filetype = filetype;
    }

    public void setActive(int active){
        this.active = active;
    }
}
