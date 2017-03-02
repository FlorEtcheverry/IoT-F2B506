import os, re, sys

FILE_RE=re.compile("^#File: (.*)$")
PARAMETER_RE=re.compile("^#Parameter: (.*)$")
SEP=" -- "

class Hash:
	""" a dictionary for primKey = name and secKey = [decription,type,use,defaultValue] """
	def __init__(self):
		self.dict={}

	def add(self,primKey,secKey,value):
		if (primKey in self.dict.keys()):
			self.dict[primKey][0]= self.dict[primKey][0] + [secKey]
			self.dict[primKey][1]= self.dict[primKey][1] + [value]
		else:
			self.dict[primKey]=[ [secKey] , [value] ]

	def check(self,primKey,secKey):
		if (primKey in self.dict.keys()):
			tmp=self.dict[primKey]
			count=0
			for i in tmp[0]:
				if (i==secKey):
					return  tmp[1][count]
				else:
					count += 1
			return ""
		else:
			return ""		


def print_header(f,ontologyName,ontologyReference):
	ontologyReference=ontologyReference.replace('\\','/')
	f.write(
"""package org.imt.atlantique.sss.kms.init;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.imt.atlantique.sss.kms.entities.LinkModel;
import org.imt.atlantique.sss.kms.entities.LinkQuery;
import org.imt.atlantique.sss.kms.entities.Request;
import org.imt.atlantique.sss.kms.entities.RequestParameter;
import org.imt.atlantique.sss.kms.entities.Sphere;
import org.imt.atlantique.sss.kms.entities.type.CachingStrategyType;

import org.imt.atlantique.sss.kms.entities.type.SharingType;

import org.imt.atlantique.sss.kms.exceptions.RequestExistException;
import org.imt.atlantique.sss.kms.factories.ResourceFactory;
import org.imt.atlantique.sss.kms.factories.type.ResourceCategory;
import org.imt.atlantique.sss.kms.manager.LinkQueryManager;
import org.imt.atlantique.sss.kms.manager.RequestManager;

import org.imt.atlantique.sss.kms.shared.entities.type.ParameterType;
import org.imt.atlantique.sss.kms.shared.entities.type.UseType;

/**
 *
 * @author admin
 */

@Stateless
public class """+ontologyName+""" extends AbstractInitSphereDB {

	@EJB
	private RequestManager requestManager;
	@EJB
	private LinkQueryManager linkQueryManager;
	@EJB
	private ResourceFactory ressourceFactory;

	@Override
	protected void buildRequest() throws RequestExistException {
		try {
			System.out.println("********** Build ********");
			//Note: insert InitSmoopleDBWithDemo.java in  line 
			//	<<this.SmoopleFactory.createSmoople(\""""+ontologyName+"""Project\", "Smoople pour le  """+ontologyName+""" Project", true, new Date(), Duration.ofDays(3*365), null);>>
			Sphere smoople = this.instantiateSphere(\""""+ontologyName+"""Project\");
			this.instantiateAuthor();
			// Referencing ontology
			URL url = new URL("""+"\"file:///"+ontologyReference+"\""+"""); 
			System.out.println("URL=" + url);
			LinkModel linkModel = (LinkModel) ressourceFactory.createResource(ResourceCategory.MODEL, "xAPI_"""+ontologyName+"""\", \""""+ontologyName+"""\", author, "1.0.1", SharingType.READ, null, url, CachingStrategyType.OFF); //TODO change  \n"""

)

def print_footer(f,ontologyName):
	f.write("""			} else
				System.out.println("Smoople is null");

		} catch (MalformedURLException ex) {
			Logger.getLogger("""+ontologyName+""".class.getName()).log(Level.SEVERE, null, ex);

		}
	}

}"""
	)


def file_info(path):
	print "Processing: "+path 
	dicts={}
	file=open(path,"r")
	l=str.strip(file.readline())
	while (l):
		f=FILE_RE.match(l)
		p=PARAMETER_RE.match(l)
		if f:
			(name,description)=str.split(f.group(1),SEP)
		if p:
			tmp=str.split(p.group(1),SEP)
			if not (len (tmp)==5 or (len (tmp)==4 and tmp[3]=="M")):
				print "ERROR: Invalid line - "+l
				assert False
			dicts[tmp[0]]=[tmp[1],tmp[2]]+(["MANDATORY"] if (tmp[3] == "M") else ["OPTIONNAL",tmp[4]])
		l=str.strip(file.readline())


	file.close()

	return [name,path,description,dicts]

def requests(folderPath):
	full=[]
	for dir,folder,files in os.walk(folderPath):
		if (dir== folderPath):
			for f in files:
				full.append(file_info(os.path.abspath(dir+"/"+f).replace('\\','/')))
	return full

def create_request(f,dict,j,hash):
	"""dict = [name,path,description,dicts] where dicts => {parameterName:[decription,type,use,defaultValue]} default value only present if use == OPTIONAL"""
	f.write("\n\n\t\t\t//***********"+ dict[0]+"***********")
	f.write("""\n			url = new URL(\"file:///"""+ dict[1]+"""");
			System.out.println("URL=" + url);
			LinkQuery linkQuery"""+str(j)+""" = (LinkQuery) ressourceFactory.createResource(ResourceCategory.QUERY, \""""+dict[0]+"\",\""+dict[2]+"""", author, "1.0.1", SharingType.READ_WRITE, null, url, CachingStrategyType.OFF);
	"""
	)
	f.write("""		Request req"""+str(j)+""" = new Request(\""""+dict[0]+"""\", \""""+dict[2]+"""\", linkQuery"""+str(j)+""");\n""")

	for i in dict[3].keys():

		value=hash.check(i,dict[3][i])
		if(value==""):
			f.write( """\n			RequestParameter """+dict[0]+"_"+i+"""Parameter = new RequestParameter(\""""+i+"""\", ParameterType."""+dict[3][i][1]+""");\n""")
			f.write( """			"""+dict[0]+"_"+i+"""Parameter.setDescription(\""""+dict[3][i][0]+"""\");\n""")
			f.write( """			"""+dict[0]+"_"+i+"""Parameter.setUse(UseType."""+dict[3][i][2]+""");\n""")
			if(dict[3][i][2]=="OPTIONNAL"):
				f.write( """			"""+dict[0]+"_"+i+"""Parameter.setDefaultValue(\""""+dict[3][i][3]+"""\");\n""")
			f.write( """			req"""+str(j)+""".addParameter("""+dict[0]+"_"+i+"Parameter"+""");\n""")
			hash.add(i,dict[3][i],dict[0]+"_"+i+"Parameter")
		else:
			f.write( """\n			req"""+str(j)+""".addParameter("""+value+""");\n""")

	f.write("""\n			requestManager.create(req1);\n""")
	
def finish_reqs(f,size):
	f.write("""\n			if (smoople != null) {
				smoople.getTemplate().getSemanticConfiguration().getResourceBundle();\n""")
	for i in xrange(1,size+1):
		f.write("""				smoople.getTemplate().getRequestList().add(req"""+str(i)+""");\n""")
	f.write("""				sphereManager.update(smoople);\n""")

def create_requests(file, reqs):
	j=0
	hash=Hash()
	for i in reqs:
		j+=1
		create_request(file,i,j,hash)
	finish_reqs(file,len(reqs))

def main():
	if len(sys.argv)!=4:
		print "Usage: python create_java.py <ontology_name> <ontology_path> <requests_path>"
	else:	
		f=open(sys.argv[1]+".java","w")
		print_header(f,sys.argv[1],os.path.abspath(sys.argv[2]))
		create_requests(f,requests(sys.argv[3]))
		print_footer(f,sys.argv[1])
		f.close()

main()