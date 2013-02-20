<%import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass;
import org.codehaus.groovy.grails.scaffolding.ScaffoldDomain
%><%=packageName ? "package ${packageName}\n\n" : ''
%>class ${className}Service {
    <%
    def domain = new DefaultGrailsDomainClass( domainClass.clazz )
    def searchFields = ScaffoldDomain.getDomainProperties(domainClass, comparator)
    %>
    def search(search, sort, order, max, offset) {
        def results = ${className}.createCriteria().list(max: max, offset: offset, sort: sort, order: order)
        <%def paramNames = ""
        if(searchFields.size() > 0){
        %>{
            or{<%
                searchFields.each { field ->
                    if(field.type == Date)
                        paramNames += "${field.name}StartDate, ${field.name}EndDate, "
                    else
                        paramNames += field.name + ", "
                    if(field.type == String){%>
                ilike("${field.name}", '%' + search + '%')<%
                    }
                    if(field.type == Short){%>
                if(search.isShort())
                    eq("${field.name}", search.toShort())<%
                    }
                    else if(field.type == Integer){%>
                if(search.isInteger())
                    eq("${field.name}", search.toInteger())<%
                    }
                    else if(field.type == Long){%>
                if(search.isLong())
                    eq("${field.name}", search.toLong())<%
                    }
                    else if(field.type == Double){%>
                if(search.isDouble())
                    eq("${field.name}", search.toDouble())<%
                    }
                }%>
            }
        }
        <%	}
        %>return [total:results.getTotalCount(), results:results]
    }

    def advancedSearch(${paramNames}sort, order, max, offset) {
        def results = ${className}.createCriteria().list(max: max, offset: offset, sort: sort, order: order)
        <%if(searchFields.size() > 0){
        %>{
            and{<%
                searchFields.eachWithIndex { field, idx ->
                    if(idx < 6){
                        if(field.type == String){%>
                if(${field.name}){
                    ilike("${field.name}", '%' + ${field.name} + '%')
                }<%
                }
                        else if(field.type == Date){
                            %>
                if(${field.name}StartDate && ${field.name}EndDate){
                    between("${field.name}", ${field.name}StartDate, ${field.name}EndDate)
                }<%
                            }
                        else{
                            %>
                if(${field.name}){
                    eq("${field.name}", ${field.name})
                }<%
                        }
                    }
                }%>
            }
        }
        <%	}
    %>return [total:results.getTotalCount(), results:results]
    }
}
