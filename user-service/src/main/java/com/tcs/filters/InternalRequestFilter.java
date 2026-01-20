package com.tcs.filters;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InternalRequestFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String internalHeader = request.getHeader("X-INTERNAL-CALL");

		if (internalHeader == null ||
		           (!internalHeader.equals("GATEWAY") && !internalHeader.equals("AUTH"))) {

		            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		            return;
		        }

        filterChain.doFilter(request, response);
		
	}

}
